package page.clab.api.domain.activityGroup.application;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.activityGroup.dao.ActivityGroupBoardRepository;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activityGroup.domain.ActivityGroupRole;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.domain.activityGroup.dto.request.ActivityGroupBoardRequestDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupBoardChildResponseDto;
import page.clab.api.domain.activityGroup.dto.response.ActivityGroupBoardResponseDto;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.domain.notification.dto.request.NotificationRequestDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.application.FileService;
import page.clab.api.global.common.file.dao.UploadFileRepository;
import page.clab.api.global.common.file.domain.UploadedFile;
import page.clab.api.global.common.file.dto.response.AssignmentFileResponseDto;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityGroupBoardService {

    private final ActivityGroupBoardRepository activityGroupBoardRepository;

    private final MemberService memberService;

    private final ActivityGroupAdminService activityGroupAdminService;

    private final ActivityGroupMemberService activityGroupMemberService;

    private final NotificationService notificationService;

    private final FileService fileService;

    @Transactional
    public Long createActivityGroupBoard(Long parentId, Long activityGroupId, ActivityGroupBoardRequestDto activityGroupBoardRequestDto) {
        Member member = memberService.getCurrentMember();
        ActivityGroup activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
        ActivityGroupBoard board = ActivityGroupBoard.of(activityGroupBoardRequestDto);
        board.setMember(member);
        board.setActivityGroup(activityGroup);
        if (parentId != null) {
            ActivityGroupBoard parentBoard = getActivityGroupBoardByIdOrThrow(parentId);
            board.setParent(parentBoard);
            parentBoard.getChildren().add(board);
            activityGroupBoardRepository.save(parentBoard);
        }

        List<String> fileUrls = activityGroupBoardRequestDto.getFileUrlList();
        if (fileUrls != null) {
            List<UploadedFile> uploadFileList =  fileUrls.stream()
                    .map(url -> fileService.getUploadedFileByUrl(url))
                    .collect(Collectors.toList());
            board.setUploadedFiles(uploadFileList);
        }
        Long id = activityGroupBoardRepository.save(board).getId();

        GroupMember groupMember = activityGroupMemberService.getGroupMemberByMemberOrThrow(member);
        if (groupMember.getRole() == ActivityGroupRole.LEADER) {
            List<GroupMember> groupMembers = activityGroupMemberService.getGroupMemberByActivityGroupId(activityGroupId);
            groupMembers.stream()
                    .forEach(gMember -> {
                        if (!Objects.equals(gMember.getMember().getId(), member.getId())) {
                            NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                                    .memberId(gMember.getMember().getId())
                                    .content("[" + activityGroup.getName() + "] " + member.getName() + "님이 새 게시글을 등록하였습니다.")
                                    .build();
                            notificationService.createNotification(notificationRequestDto);
                        }
                    });
        } else {
            GroupMember groupLeader = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroupId, ActivityGroupRole.LEADER);
            NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                    .memberId(groupLeader.getMember().getId())
                    .content("[" + activityGroup.getName() + "] " + member.getName() + "님이 새 게시글을 등록하였습니다.")
                    .build();
            notificationService.createNotification(notificationRequestDto);
        }
        return id;
    }

    public PagedResponseDto<ActivityGroupBoardResponseDto> getAllActivityGroupBoard(Pageable pageable) {
        List<ActivityGroupBoard> boards = activityGroupBoardRepository.findAllByOrderByCreatedAtAsc();
        List<ActivityGroupBoardResponseDto> activityGroupBoardResponseDtos = boards.stream()
                .map(board -> toActivityGroupBoardResponseDto(board))
                .collect(Collectors.toList());
        Page<ActivityGroupBoardResponseDto> pagedResponseDto = new PageImpl<>(activityGroupBoardResponseDtos, pageable, activityGroupBoardResponseDtos.size());
        return new PagedResponseDto<>(pagedResponseDto);
    }

    public ActivityGroupBoardResponseDto getActivityGroupBoardById(Long activityGroupBoardId) {
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);
        return toActivityGroupBoardResponseDto(board);
    }

    public PagedResponseDto<ActivityGroupBoardChildResponseDto> getActivityGroupBoardByParent(Long parentId, Pageable pageable) {
        List<ActivityGroupBoard> boards = getChildBoards(parentId);
        List<ActivityGroupBoardChildResponseDto> activityGroupBoardChildResponseDtos = boards.stream()
                .map(board -> toActivityGroupBoardChildResponseDto(board))
                .collect(Collectors.toList());
        Page<ActivityGroupBoardChildResponseDto> pagedResponseDto = new PageImpl<>(activityGroupBoardChildResponseDtos, pageable, activityGroupBoardChildResponseDtos.size());
        return new PagedResponseDto<>(pagedResponseDto);
    }

    public Long updateActivityGroupBoard(Long activityGroupBoardId, ActivityGroupBoardRequestDto activityGroupBoardRequestDto) {
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);
        board.setCategory(activityGroupBoardRequestDto.getCategory());
        board.setTitle(activityGroupBoardRequestDto.getTitle());
        board.setContent(activityGroupBoardRequestDto.getContent());

        List<String> fileUrls = activityGroupBoardRequestDto.getFileUrlList();
        if (fileUrls != null) {
            List<UploadedFile> uploadFileList =  fileUrls.stream()
                    .map(url -> fileService.getUploadedFileByUrl(url))
                    .collect(Collectors.toList());
            board.setUploadedFiles(uploadFileList);
        }

        return activityGroupBoardRepository.save(board).getId();
    }

    public Long deleteActivityGroupBoard(Long activityGroupBoardId) {
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);
        activityGroupBoardRepository.delete(board);
        return board.getId();
    }

    private ActivityGroupBoard getActivityGroupBoardByIdOrThrow(Long activityGroupBoardId) {
        return activityGroupBoardRepository.findById(activityGroupBoardId)
                .orElseThrow(() -> new NotFoundException("해당 게시글을 찾을 수 없습니다."));
    }

    private List<ActivityGroupBoard> getChildBoards(Long activityGroupBoardId) {
        List<ActivityGroupBoard> boardList = new ArrayList<>();
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);
        if (board.getParent() == null || board.getChildren() != null) {
            boardList.add(board);
            for (ActivityGroupBoard child : board.getChildren()) {
                boardList.addAll(getChildBoards(child.getId()));
            }
        } else {
            boardList.add(board);
        }
        boardList.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
        return boardList;
    }

        public ActivityGroupBoardChildResponseDto toActivityGroupBoardChildResponseDto(ActivityGroupBoard board) {
            ActivityGroupBoardChildResponseDto activityGroupBoardChildResponseDto = ActivityGroupBoardChildResponseDto.of(board);

            if (board.getUploadedFiles() != null) {
                List<String> fileUrls = board.getUploadedFiles().stream()
                        .map(file -> file.getUrl()).collect(Collectors.toList());

                List<AssignmentFileResponseDto> fileResponseDtos = fileUrls.stream()
                        .map(url -> AssignmentFileResponseDto.builder()
                                .fileUrl(url)
                                .originalFileName(fileService.getOriginalFileNameByUrl(url))
                                .storageDateTimeOfFile(fileService.getStorageDateTimeOfFile(url))
                                .build())
                        .collect(Collectors.toList());

                activityGroupBoardChildResponseDto.setFileResponseDtoList(fileResponseDtos);
            }

            if (board.getChildren() != null && !board.getChildren().isEmpty()) {

                List<ActivityGroupBoardChildResponseDto> childrenDtoList = new ArrayList<>();
                for (ActivityGroupBoard child : board.getChildren()) {
                    childrenDtoList.add(toActivityGroupBoardChildResponseDto(child));
                }

                activityGroupBoardChildResponseDto.setChildren(childrenDtoList);
            }

            return activityGroupBoardChildResponseDto;
        }

    public ActivityGroupBoardResponseDto toActivityGroupBoardResponseDto(ActivityGroupBoard board) {
        ActivityGroupBoardResponseDto activityGroupBoardResponseDto = ActivityGroupBoardResponseDto.of(board);

        if (board.getUploadedFiles() != null) {
            List<String> fileUrls = board.getUploadedFiles().stream()
                    .map(file -> file.getUrl()).collect(Collectors.toList());

            List<AssignmentFileResponseDto> fileResponseDtos = fileUrls.stream()
                            .map(url -> AssignmentFileResponseDto.builder()
                                    .fileUrl(url)
                                    .originalFileName(fileService.getOriginalFileNameByUrl(url))
                                    .storageDateTimeOfFile(fileService.getStorageDateTimeOfFile(url))
                                    .build())
                            .collect(Collectors.toList());

            activityGroupBoardResponseDto.setAssignmentFileResponseDtoList(fileResponseDtos);
        }
        return activityGroupBoardResponseDto;
    }

}