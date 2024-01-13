package page.clab.api.type.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SharedAccountUsageRequestDto {

    @NotNull(message = "{notNull.sharedAccountUsage.sharedAccountId}")
    @Schema(description = "공유 계정 식별 아이디(계정 아이디 X)", example = "1", required = true)
    private Long sharedAccountId;

    @NotNull(message = "{notNull.sharedAccountUsage.startTime}}")
    @Schema(description = "계정 이용 시작 시간", example = "2023-11-06T00:00:00", required = true)
    private LocalDateTime startTime;

    @NotNull(message = "{notNull.sharedAccountUsage.endTime}}")
    @Schema(description = "계정 이용 종료 시간", example = "2023-11-06T00:00:00", required = true)
    private LocalDateTime endTime;

}