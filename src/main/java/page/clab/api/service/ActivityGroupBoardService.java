package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.repository.ActivityGroupBoardRepository;
import page.clab.api.type.dto.ActivityGroupBoardChildResponseDto;
import page.clab.api.type.dto.ActivityGroupBoardRequestDto;
import page.clab.api.type.dto.ActivityGroupBoardResponseDto;
import page.clab.api.type.dto.NotificationRequestDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.entity.ActivityGroup;
import page.clab.api.type.entity.ActivityGroupBoard;
import page.clab.api.type.entity.GroupMember;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.ActivityGroupRole;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ActivityGroupBoardService {

    private final ActivityGroupBoardRepository activityGroupBoardRepository;

    private final MemberService memberService;

    private final ActivityGroupAdminService activityGroupAdminService;

    private final ActivityGroupMemberService activityGroupMemberService;

    private final NotificationService notificationService;

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
        Long id = activityGroupBoardRepository.save(board).getId();

        GroupMember groupMember = activityGroupMemberService.getGroupMemberByMemberOrThrow(member);
        if (groupMember.getRole() == ActivityGroupRole.LEADER) {
            List<GroupMember> groupMembers = activityGroupMemberService.getGroupMemberByActivityGroupId(activityGroupId);
            for (GroupMember gMember : groupMembers) {
                if (!Objects.equals(gMember.getMember().getId(), member.getId())) {
                    NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                            .memberId(gMember.getMember().getId())
                            .content("활동 그룹 팀장이 " + activityGroupBoardRequestDto.getTitle() +" 게시글을 등록하였습니다.")
                            .build();
                    notificationService.createNotification(notificationRequestDto);
                }
            }
        }else {
            GroupMember groupLeader = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroupId, ActivityGroupRole.LEADER);
            NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                    .memberId(groupLeader.getMember().getId())
                    .content("활동 그룹 팀원이 " + activityGroupBoardRequestDto.getTitle() + " 게시글을 등록하였습니다.")
                    .build();
            notificationService.createNotification(notificationRequestDto);
        }
        return id;
    }

    public PagedResponseDto<ActivityGroupBoardResponseDto> getAllActivityGroupBoard(Pageable pageable) {
        Page<ActivityGroupBoard> boards = activityGroupBoardRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(boards.map(ActivityGroupBoardResponseDto::of));
    }

    public ActivityGroupBoardResponseDto getActivityGroupBoardById(Long activityGroupBoardId) {
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);
        return ActivityGroupBoardResponseDto.of(board);
    }

    public PagedResponseDto<ActivityGroupBoardChildResponseDto> getActivityGroupBoardByParent(Long parentId, Pageable pageable) {
        List<ActivityGroupBoard> boardList = getChildBoards(parentId);
        Page<ActivityGroupBoard> boardPage = new PageImpl<>(boardList, pageable, boardList.size());
        return new PagedResponseDto<>(boardPage.map(ActivityGroupBoardChildResponseDto::of));
    }

    public Long updateActivityGroupBoard(Long activityGroupBoardId, ActivityGroupBoardRequestDto activityGroupBoardRequestDto) {
        ActivityGroupBoard board = getActivityGroupBoardByIdOrThrow(activityGroupBoardId);
        board.setCategory(activityGroupBoardRequestDto.getCategory());
        board.setTitle(activityGroupBoardRequestDto.getTitle());
        board.setContent(activityGroupBoardRequestDto.getContent());
        board.setFilePath(activityGroupBoardRequestDto.getFilePath());
        board.setFileName(activityGroupBoardRequestDto.getFileName());
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
        if (board.getParent() == null || board.getChildren()!= null) {
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

}