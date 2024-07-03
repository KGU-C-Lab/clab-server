package page.clab.api.domain.notification.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.MemberLookupUseCase;
import page.clab.api.domain.notification.application.port.in.NotificationsRetrievalUseCase;
import page.clab.api.domain.notification.application.port.out.RetrieveNotificationsByMemberIdPort;
import page.clab.api.domain.notification.domain.Notification;
import page.clab.api.domain.notification.dto.response.NotificationResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class NotificationsRetrievalService implements NotificationsRetrievalUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final RetrieveNotificationsByMemberIdPort retrieveNotificationsByMemberIdPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<NotificationResponseDto> retrieve(Pageable pageable) {
        String currentMemberId = memberLookupUseCase.getCurrentMemberId();
        Page<Notification> notifications = retrieveNotificationsByMemberIdPort.findByMemberId(currentMemberId, pageable);
        return new PagedResponseDto<>(notifications.map(NotificationResponseDto::toDto));
    }
}
