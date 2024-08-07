package page.clab.api.domain.activity.activitygroup.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupCategory;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activity.activitygroup.domain.GroupMember;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ActivityGroupStudyResponseDto {

    private Long id;
    private ActivityGroupCategory category;
    private String subject;
    private String name;
    private String content;
    private ActivityGroupStatus status;
    private String imageUrl;
    private List<GroupMemberResponseDto> groupMembers;
    private String curriculum;
    private List<ActivityGroupBoardResponseDto> activityGroupBoards;

    @JsonProperty("isOwner")
    private boolean isOwner;
    private LocalDateTime createdAt;

    public static ActivityGroupStudyResponseDto create(ActivityGroup activityGroup, List<GroupMember> groupMembers,
                                                       List<ActivityGroupBoardResponseDto> boards, List<GroupMemberResponseDto> groupMemberResponseDtos, boolean isOwner) {
        return ActivityGroupStudyResponseDto.builder()
                .id(activityGroup.getId())
                .category(activityGroup.getCategory())
                .subject(activityGroup.getSubject())
                .name(activityGroup.getName())
                .content(activityGroup.getContent())
                .status(activityGroup.getStatus())
                .imageUrl(activityGroup.getImageUrl())
                .groupMembers(groupMemberResponseDtos)
                .curriculum(activityGroup.getCurriculum())
                .activityGroupBoards(boards)
                .isOwner(isOwner)
                .createdAt(activityGroup.getCreatedAt())
                .build();
    }
}
