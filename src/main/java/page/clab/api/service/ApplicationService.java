package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.ApplicationRepository;
import page.clab.api.type.dto.ApplicationPassResponseDto;
import page.clab.api.type.dto.ApplicationRequestDto;
import page.clab.api.type.dto.ApplicationResponseDto;
import page.clab.api.type.dto.NotificationRequestDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.entity.Application;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.Role;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final MemberService memberService;

    private final NotificationService notificationService;

    private final ApplicationRepository applicationRepository;

    public void createApplication(ApplicationRequestDto appRequestDto) {
        Application application = Application.of(appRequestDto);
        application.setContact(memberService.removeHyphensFromContact(application.getContact()));
        application.setIsPass(false);
        application.setUpdateTime(LocalDateTime.now());
        applicationRepository.save(application);

        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .memberId(appRequestDto.getStudentId())
                .content("동아리 가입 신청이 접수되었습니다. 관리자가 승인하면 가입이 완료됩니다.")
                .build();
        notificationService.createNotification(notificationRequestDto);

        List<Member> superMembers = memberService.getMembersByRole(Role.SUPER);
        for (Member superMember : superMembers) {
            NotificationRequestDto notificationRequestDtoForSuper = NotificationRequestDto.builder()
                    .memberId(superMember.getId())
                    .content(appRequestDto.getName() + "님dl 동아리 가입을 신청하였습니다.")
                    .build();
            notificationService.createNotification(notificationRequestDtoForSuper);
        }
    }

    public PagedResponseDto<ApplicationResponseDto> getApplications(Pageable pageable) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Page<Application> applications = applicationRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(applications.map(ApplicationResponseDto::of));
    }

    public PagedResponseDto<ApplicationResponseDto> getApplicationsBetweenDates(LocalDate startDate, LocalDate endDate, Pageable pageable) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        Page<Application> applicationsBetweenDates = getApplicationByUpdateTimeBetween(pageable, startDateTime, endDateTime);
        return new PagedResponseDto<>(applicationsBetweenDates.map(ApplicationResponseDto::of));
    }

    public ApplicationResponseDto searchApplication(String applicationId) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Application application = null;
        if (applicationId != null) {
            application = getApplicationByIdOrThrow(applicationId);
        }
        return ApplicationResponseDto.of(application);
    }

    @Transactional
    public void approveApplication(String applicationId) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Application application = getApplicationByIdOrThrow(applicationId);
        if (application.getIsPass()) {
            application.setIsPass(false);
            application.setUpdateTime(LocalDateTime.now());
            applicationRepository.save(application);
        } else {
            application.setIsPass(true);
            application.setUpdateTime(LocalDateTime.now());
            applicationRepository.save(application);
        }

        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .memberId(application.getStudentId())
                .content("동아리 가입 신청이 승인되었습니다. 가입을 축하드립니다!")
                .build();
        notificationService.createNotification(notificationRequestDto);
    }

    @Transactional
    public PagedResponseDto<ApplicationResponseDto> getApprovedApplications(Pageable pageable) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Page<Application> applications = getApplicationByIsPass(pageable);
        if (applications.isEmpty()) {
            throw new NotFoundException("승인된 신청자가 없습니다.");
        } else {
            return new PagedResponseDto<>(applications.map(ApplicationResponseDto::of));
        }
    }

    public ApplicationPassResponseDto getApplicationPass(String applicationId) {
        Application application = getApplicationById(applicationId);
        if (application == null) {
            return ApplicationPassResponseDto.builder()
                    .isPass(false)
                    .build();
        }
        return ApplicationPassResponseDto.of(application);
    }

    public void deleteApplication(String applicationId) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Application application = getApplicationByIdOrThrow(applicationId);
        applicationRepository.delete(application);
    }

    private Application getApplicationByIdOrThrow(String applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("해당 신청자가 없습니다."));
    }

    private Application getApplicationById(String applicationId) {
        return applicationRepository.findById(applicationId)
                .orElse(null);
    }

    private Page<Application> getApplicationByUpdateTimeBetween(Pageable pageable, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return applicationRepository.findAllByUpdateTimeBetweenOrderByCreatedAtDesc(startDateTime, endDateTime, pageable);
    }

    private Page<Application> getApplicationByIsPass(Pageable pageable) {
        return applicationRepository.findAllByIsPassOrderByCreatedAtDesc(true, pageable);
    }

}
