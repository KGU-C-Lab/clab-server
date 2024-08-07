package page.clab.api.domain.activity.activitygroup.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupCategory;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupStatus;
import page.clab.api.domain.activity.activitygroup.domain.GroupMember;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ActivityGroupProjectResponseDto {

    private Long id;
    private ActivityGroupCategory category;
    private String subject;
    private String name;
    private String content;
    private ActivityGroupStatus status;
    private String imageUrl;
    private List<GroupMemberResponseDto> groupMembers;
    private LocalDate startDate;
    private LocalDate endDate;
    private String techStack;
    private String githubUrl;
    private List<ActivityGroupBoardResponseDto> activityGroupBoards;

    @JsonProperty("isOwner")
    private boolean isOwner;
    private LocalDateTime createdAt;

    public static ActivityGroupProjectResponseDto create(ActivityGroup activityGroup, List<GroupMember> groupMembers,
                                                         List<ActivityGroupBoardResponseDto> boards, List<GroupMemberResponseDto> groupMemberResponseDtos, boolean isOwner) {
        return ActivityGroupProjectResponseDto.builder()
                .id(activityGroup.getId())
                .category(activityGroup.getCategory())
                .subject(activityGroup.getSubject())
                .name(activityGroup.getName())
                .content(activityGroup.getContent())
                .status(activityGroup.getStatus())
                .imageUrl(activityGroup.getImageUrl())
                .groupMembers(groupMemberResponseDtos)
                .startDate(activityGroup.getStartDate())
                .endDate(activityGroup.getEndDate())
                .techStack(activityGroup.getTechStack())
                .githubUrl(activityGroup.getGithubUrl())
                .activityGroupBoards(boards)
                .isOwner(isOwner)
                .createdAt(activityGroup.getCreatedAt())
                .build();
    }
}
