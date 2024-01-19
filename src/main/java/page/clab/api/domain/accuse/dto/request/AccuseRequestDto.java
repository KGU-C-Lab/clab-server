package page.clab.api.domain.accuse.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccuseRequestDto {

    @NotNull(message = "{notNull.accuse.targetType}")
    @Schema(description = "신고 대상 유형", example = "BOARD", required = true)
    private TargetType targetType;

    @NotNull(message = "{notNull.accuse.targetId}")
    @Schema(description = "신고 대상 ID", example = "1", required = true)
    private Long targetId;

    @NotNull(message = "{notNull.accuse.reason}")
    @Size(min = 1, max = 1000, message = "{size.accuse.reason}")
    @Schema(description = "신고 사유", example = "부적절한 게시글입니다.", required = true)
    private String reason;

    public static AccuseRequestDto of(Accuse accuse) {
        return ModelMapperUtil.getModelMapper().map(accuse, AccuseRequestDto.class);
    }

}