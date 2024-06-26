package page.clab.api.domain.notification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.domain.Notification;

@Getter
@Setter
public class NotificationRequestDto {

    @NotNull(message = "{notNull.notification.memberId}")
    @Schema(description = "회원 아이디", example = "202312000", required = true)
    private String memberId;

    @NotNull(message = "{notNull.notification.content}")
    @Schema(description = "내용", example = "알림 내용", required = true)
    private String content;

    public static Notification toEntity(NotificationRequestDto requestDto, Member member) {
        return Notification.builder()
                .content(requestDto.getContent())
                .member(member)
                .build();
    }

}
