package page.clab.api.domain.notification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class NotificationRequestDto {

    @NotNull(message = "{notNull.notification.memberId}")
    @Size(min = 1, message = "{size.notification.memberId}")
    @Schema(description = "회원 아이디", example = "202312000", required = true)
    private String memberId;

    @NotNull(message = "{notNull.notification.content}")
    @Size(min = 1, max = 1000, message = "{size.notification.content}")
    @Schema(description = "내용", example = "알림 내용", required = true)
    private String content;

}