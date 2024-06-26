package page.clab.api.domain.activityGroup.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activityGroup.dao.ActivityGroupBoardRepository;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoardCategory;
import page.clab.api.domain.activityGroup.domain.ActivityGroupRole;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupBoardRequestDto;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupBoardUpdateRequestDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupBoardChildResponseDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupBoardResponseDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupBoardUpdateResponseDto;
import page.clab.api.domain.activityGroup.dto.response.AssignmentSubmissionWithFeedbackResponseDto;
import page.clab.api.domain.activityGroup.dto.response.FeedbackResponseDto;
import page.clab.api.domain.activityGroup.exception.InvalidParentBoardException;
import page.clab.api.domain.award.domain.Award;
import page.clab.api.domain.award.dto.response.AwardResponseDto;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.application.UploadedFileService;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityGroupBoardService {

    private final ActivityGroupBoardRepository activityGroupBoardRepository;

    private final MemberService memberService;

    private final ActivityGroupAdminService activityGroupAdminService;

    private final ActivityGroupMemberService activityGroupMemberService;

    private final NotificationService notificationService;

    private final ValidationService validationService;

    private final UploadedFileService uploadedFileService;

    @Transactional
    public Long createActivityGroupBoard(Long parentId, Long activityGroupId, ActivityGroupBoardRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
        if (!activityGroupMemberService.isGroupMember(activityGroup, currentMember)) {
            throw new PermissionDeniedException("활동 그룹 멤버만 게시글을 등록할 수 있습니다.");
        }

        validateParentBoard(requestDto.getCategory(), parentId);
        List<UploadedFile> uploadedFiles = uploadedFileService.getUploadedFilesByUrls(requestDto.getFileUrls());

        ActivityGroupBoard parentBoard = parentId != null ? getActivityGroupBoardByIdOrThrow(parentId) : null;
        ActivityGroupBoard board = ActivityGroupBoardRequestDto.toEntity(requestDto, currentMember, activityGroup, parentBoard, uploadedFiles);
        validationService.checkValid(board);
        if (parentId != null) {
            parentBoard.addChild(board);
            activityGroupBoardRepository.save(parentBoard);
        }
        activityGroupBoardRepository.save(board);

        notifyMembersAboutNewBoard(activityGroupId, activityGroup, currentMember);
        return board.getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupBoardResponseDto> getAllActivityGroupBoard(Pageable pageable) {
        Page<ActivityGroupBoard> boards = activityGroupBoardRepository.findAll(pageable);
        return new PagedResponseDto<>(boards.map(ActivityGroupBoardResponseDto::toDto));
    }

    @Transactional(readOnly = true)
    public ActivityGroupBoardResponseDto getActivityGroupBoardById(Long activityGroupBoardId) {
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);
        return ActivityGroupBoardResponseDto.toDto(board);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupBoardResponseDto> getActivityGroupBoardByCategory(Long activityGroupId, ActivityGroupBoardCategory category, Pageable pageable) {
        Page<ActivityGroupBoard> boards = activityGroupBoardRepository.findAllByActivityGroup_IdAndCategory(activityGroupId, category, pageable);
        return new PagedResponseDto<>(boards.map(ActivityGroupBoardResponseDto::toDto));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupBoardChildResponseDto> getActivityGroupBoardByParent(Long parentId, Pageable pageable) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        ActivityGroupBoard parentBoard = getActivityGroupBoardByIdOrThrow(parentId);
        Long activityGroupId = parentBoard.getActivityGroup().getId();

        GroupMember groupLeader = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroupId, ActivityGroupRole.LEADER);
        parentBoard.validateAccessPermission(currentMember, groupLeader);

        List<ActivityGroupBoard> childBoards = getChildBoards(parentId);
        Page<ActivityGroupBoard> boards = new PageImpl<>(childBoards, pageable, childBoards.size());
        return new PagedResponseDto<>(boards.map(ActivityGroupBoardChildResponseDto::toDto));
    }

    @Transactional(readOnly = true)
    public List<AssignmentSubmissionWithFeedbackResponseDto> getMyAssignmentsWithFeedbacks(Long parentId) {
        Member currentMember = memberService.getCurrentMember();
        ActivityGroupBoard parentBoard = getActivityGroupBoardByIdOrThrow(parentId);
        List<ActivityGroupBoard> mySubmissions = activityGroupBoardRepository.findMySubmissionsWithFeedbacks(parentId, currentMember.getId());
        return mySubmissions.stream()
                .map(submission -> {
                    List<FeedbackResponseDto> feedbackDtos = submission.getChildren().stream()
                            .filter(ActivityGroupBoard::isFeedback)
                            .map(FeedbackResponseDto::toDto)
                            .toList();
                    return AssignmentSubmissionWithFeedbackResponseDto.toDto(submission, feedbackDtos);
                })
                .toList();
    }

    @Transactional
    public ActivityGroupBoardUpdateResponseDto updateActivityGroupBoard(Long activityGroupBoardId, ActivityGroupBoardUpdateRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);
        board.validateAccessPermission(currentMember);

        board.update(requestDto, uploadedFileService);
        validationService.checkValid(board);
        ActivityGroupBoard savedBoard = activityGroupBoardRepository.save(board);
        return ActivityGroupBoardUpdateResponseDto.toDto(savedBoard);
    }

    public Long deleteActivityGroupBoard(Long activityGroupBoardId) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);
        board.validateAccessPermission(currentMember);
        activityGroupBoardRepository.delete(board);
        return board.getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ActivityGroupBoardResponseDto> getDeletedActivityGroupBoards(Pageable pageable) {
        Page<ActivityGroupBoard> activityGroupBoards = activityGroupBoardRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(activityGroupBoards.map(ActivityGroupBoardResponseDto::toDto));
    }

    private ActivityGroupBoard getActivityGroupBoardByIdOrThrow(Long activityGroupBoardId) {
        return activityGroupBoardRepository.findById(activityGroupBoardId)
                .orElseThrow(() -> new NotFoundException("해당 게시글을 찾을 수 없습니다."));
    }

    private List<ActivityGroupBoard> getChildBoards(Long activityGroupBoardId) {
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);
        List<ActivityGroupBoard> children = activityGroupBoardRepository.findAllChildrenByParentId(activityGroupBoardId);
        children.sort(Comparator.comparing(ActivityGroupBoard::getCreatedAt).reversed());
        return children;
    }

    private void validateParentBoard(ActivityGroupBoardCategory category, Long parentId) throws InvalidParentBoardException {
        if ((category == ActivityGroupBoardCategory.NOTICE || category == ActivityGroupBoardCategory.WEEKLY_ACTIVITY)) {
            if (parentId != null) {
                throw new InvalidParentBoardException(category.getDescription() + " 게시물은 부모 게시판을 가질 수 없습니다.");
            } else {
                return;
            }
        }

        if ((category == ActivityGroupBoardCategory.ASSIGNMENT || category == ActivityGroupBoardCategory.SUBMIT || category == ActivityGroupBoardCategory.FEEDBACK) && parentId == null) {
            throw new InvalidParentBoardException(category.getDescription() + " 게시물은 부모 게시판이 필요합니다.");
        }

        ActivityGroupBoard parentBoard = getActivityGroupBoardByIdOrThrow(parentId);

        ActivityGroupBoardCategory expectedParentCategory = switch (category) {
            case ASSIGNMENT -> ActivityGroupBoardCategory.WEEKLY_ACTIVITY;
            case SUBMIT -> ActivityGroupBoardCategory.ASSIGNMENT;
            case FEEDBACK -> ActivityGroupBoardCategory.SUBMIT;
            default -> throw new InvalidParentBoardException("유효하지 않은 카테고리입니다.");
        };

        if (parentBoard.getCategory() != expectedParentCategory) {
            String message = switch (category) {
                case ASSIGNMENT -> "과제의 부모 게시판은 주차별 활동 게시판이어야 합니다.";
                case SUBMIT -> "제출의 부모 게시판은 과제 게시판이어야 합니다.";
                case FEEDBACK -> "피드백의 부모 게시판은 제출 게시판이어야 합니다.";
                default -> "유효하지 않은 카테고리입니다.";
            };
            throw new InvalidParentBoardException(message);
        }
    }

    private void notifyMembersAboutNewBoard(Long activityGroupId, ActivityGroup activityGroup, Member member) {
        GroupMember groupMember = activityGroupMemberService.getGroupMemberByActivityGroupAndMemberOrThrow(activityGroup, member);
        if (groupMember.isLeader()) {
            List<GroupMember> groupMembers = activityGroupMemberService.getGroupMemberByActivityGroupId(activityGroupId);
            groupMembers
                    .forEach(gMember -> {
                        if (!gMember.isOwner(member)) {
                            notificationService.sendNotificationToMember(gMember.getMember(), "[" + activityGroup.getName() + "] " + member.getName() + "님이 새 게시글을 등록하였습니다.");
                        }
                    });
        } else {
            GroupMember groupLeader = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroupId, ActivityGroupRole.LEADER);
            if (groupLeader != null) {
                notificationService.sendNotificationToMember(groupLeader.getMember(), "[" + activityGroup.getName() + "] " + member.getName() + "님이 새 게시글을 등록하였습니다.");
            }
        }
    }

}