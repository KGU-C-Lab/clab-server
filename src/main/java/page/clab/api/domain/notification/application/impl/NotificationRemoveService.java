package page.clab.api.domain.notification.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.notification.application.NotificationRemoveUseCase;
import page.clab.api.domain.notification.dao.NotificationRepository;
import page.clab.api.domain.notification.domain.Notification;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class NotificationRemoveService implements NotificationRemoveUseCase {

    private final NotificationRepository notificationRepository;

    @Transactional
    @Override
    public Long remove(Long notificationId) throws PermissionDeniedException {
        Notification notification = getNotificationByIdOrThrow(notificationId);
        notification.validateAccessPermission(notification.getMemberId());
        notification.delete();
        return notificationRepository.save(notification).getId();
    }

    private Notification getNotificationByIdOrThrow(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 알림입니다."));
    }
}