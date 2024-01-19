package page.clab.api.domain.activityGroup.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupCategory;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityGroupRequestDto {

    @NotNull(message = "{notnull.activityGroup.category}")
    private ActivityGroupCategory category;

    @NotNull(message = "{notnull.activityGroup.name}")
    @Size(min = 1, max = 30, message = "{size.activityGroup.name}")
    @Schema(description = "활동명", example = "2024-1 신입생 대상 C언어 스터디")
    private String name;

    @NotNull(message = "{notnull.activityGroup.content}")
    @Size(min = 1, max = 1000, message = "{size.activityGroup.content}")
    @Schema(description = "활동 설명", example = "2024-1 신입생 대상 C언어 스터디")
    private String content;

    @URL(message = "{url.activityGroup.imageUrl}")
    @Schema(description = "활동 이미지 URL", example = "https://i.namu.wiki/i/KcqDuQYTxNpUcLIMZTg28QXse0XiWx1G7K68kYYCo1GuhoHmhB_V8Qe9odGGt0BH9-0nQZTN53WXTNpDmwVfWQ.svg")
    private String imageUrl;

    @Schema(description = "커리큘럼", example = "큐, 스택, 리스트, 연결리스트, 트리, 그래프")
    private String curriculum;

    @Schema(description = "활동 시작일", example = "2023-03-02")
    private LocalDate startDate;

    @Schema(description = "활동 종료일", example = "2023-06-18")
    private LocalDate endDate;

    @Schema(description = "기술 스택", example = "Unreal Engine, C#")
    private String techStack;

    @URL(message = "{url.activityGroup.githubUrl}")
    @Schema(description = "Github URL", example = "https://github.com/KGU-C-Lab")
    private String githubUrl;

    public static ActivityGroupRequestDto of(ActivityGroup activityGroup) {
        return ModelMapperUtil.getModelMapper().map(activityGroup, ActivityGroupRequestDto.class);
    }

}