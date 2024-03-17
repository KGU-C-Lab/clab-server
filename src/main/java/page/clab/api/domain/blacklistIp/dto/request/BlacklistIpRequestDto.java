package page.clab.api.domain.blacklistIp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
public class BlacklistIpRequestDto {

    @NotNull(message = "{notNull.blacklistIp.ipAddress}")
    @Schema(description = "블랙리스트 IP 주소", example = "0.0.0.0", required = true)
    private String ipAddress;

    @Schema(description = "블랙리스트 사유", example = "스팸")
    private String reason;

}