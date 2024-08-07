package page.clab.api.domain.activity.activitygroup.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroupBoardCategory;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.common.file.dto.response.UploadedFileResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ActivityGroupBoardChildResponseDto {

    private Long id;
    private String memberId;
    private String memberName;
    private ActivityGroupBoardCategory category;
    private String title;
    private String content;
    private LocalDateTime dueDateTime;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private List<UploadedFileResponseDto> files;
    private List<ActivityGroupBoardChildResponseDto> children;

    public static ActivityGroupBoardChildResponseDto toDto(ActivityGroupBoard board, MemberBasicInfoDto memberBasicInfoDto, List<ActivityGroupBoardChildResponseDto> childrenDtos) {
        return ActivityGroupBoardChildResponseDto.builder()
                .id(board.getId())
                .memberId(memberBasicInfoDto.getMemberId())
                .memberName(memberBasicInfoDto.getMemberName())
                .category(board.getCategory())
                .title(board.getTitle())
                .content(board.getContent())
                .dueDateTime(board.getDueDateTime())
                .updatedAt(board.getUpdatedAt())
                .createdAt(board.getCreatedAt())
                .files(UploadedFileResponseDto.toDto(board.getUploadedFiles()))
                .children(childrenDtos)
                .build();
    }
}
