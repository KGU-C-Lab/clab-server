package page.clab.api.domain.memberManagement.notification.application.port.out;

import page.clab.api.domain.memberManagement.notification.domain.Notification;

import java.util.List;

public interface RegisterNotificationPort {

    Notification save(Notification notification);

    void saveAll(List<Notification> notifications);
}
