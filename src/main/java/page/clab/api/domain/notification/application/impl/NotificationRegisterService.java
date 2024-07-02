package page.clab.api.domain.notification.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.notification.application.NotificationRegisterUseCase;
import page.clab.api.domain.notification.dao.NotificationRepository;
import page.clab.api.domain.notification.domain.Notification;
import page.clab.api.domain.notification.dto.request.NotificationRequestDto;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class NotificationRegisterService implements NotificationRegisterUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final ValidationService validationService;
    private final NotificationRepository notificationRepository;

    @Transactional
    @Override
    public Long register(NotificationRequestDto requestDto) {
        memberLookupUseCase.ensureMemberExists(requestDto.getMemberId());
        Notification notification = NotificationRequestDto.toEntity(requestDto);
        validationService.checkValid(notification);
        return notificationRepository.save(notification).getId();
    }
}