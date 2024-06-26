package page.clab.api.domain.sharedAccount.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SharedAccountUsageRequestDto {

    @NotNull(message = "{notNull.sharedAccountUsage.sharedAccountId}")
    @Schema(description = "공유 계정 식별 아이디(계정 아이디 X)", example = "1", required = true)
    private Long sharedAccountId;

    @Schema(description = "계정 이용 시작 시간", example = "2023-11-06T00:00:00")
    private LocalDateTime startTime;

    @NotNull(message = "{notNull.sharedAccountUsage.endTime}}")
    @Schema(description = "계정 이용 종료 시간", example = "2023-11-06T00:00:00", required = true)
    private LocalDateTime endTime;

}
