package page.clab.api.domain.memberManagement.member.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.request.MemberRequestDto;
import page.clab.api.domain.memberManagement.member.application.exception.DuplicateMemberContactException;
import page.clab.api.domain.memberManagement.member.application.exception.DuplicateMemberEmailException;
import page.clab.api.domain.memberManagement.member.application.exception.DuplicateMemberIdException;
import page.clab.api.domain.memberManagement.member.application.port.in.RegisterMemberUseCase;
import page.clab.api.domain.memberManagement.member.application.port.out.CheckMemberExistencePort;
import page.clab.api.domain.memberManagement.member.application.port.out.RegisterMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.memberManagement.position.domain.Position;
import page.clab.api.domain.memberManagement.position.domain.PositionType;
import page.clab.api.external.memberManagement.position.application.port.ExternalRegisterPositionUseCase;
import page.clab.api.external.memberManagement.position.application.port.ExternalRetrievePositionUseCase;
import page.clab.api.global.common.email.application.EmailService;
import page.clab.api.global.common.verification.application.VerificationService;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberRegisterService implements RegisterMemberUseCase {

    private final CheckMemberExistencePort checkMemberExistencePort;
    private final RegisterMemberPort registerMemberPort;
    private final ExternalRegisterPositionUseCase externalRegisterPositionUseCase;
    private final ExternalRetrievePositionUseCase externalRetrievePositionUseCase;
    private final VerificationService verificationService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public String registerMember(MemberRequestDto requestDto) {
        checkMemberUniqueness(requestDto);
        Member member = MemberRequestDto.toEntity(requestDto);
        setupMemberPassword(member);
        registerMemberPort.save(member);
        createPositionByMember(member);
        return member.getId();
    }

    private void checkMemberUniqueness(MemberRequestDto requestDto) {
        if (checkMemberExistencePort.existsById(requestDto.getId()))
            throw new DuplicateMemberIdException("이미 사용 중인 아이디입니다.");
        if (checkMemberExistencePort.existsByContact(requestDto.getContact()))
            throw new DuplicateMemberContactException("이미 사용 중인 연락처입니다.");
        if (checkMemberExistencePort.existsByEmail(requestDto.getEmail()))
            throw new DuplicateMemberEmailException("이미 사용 중인 이메일입니다.");
    }

    private void setupMemberPassword(Member member) {
        if (member.getPassword().isEmpty()) {
            setRandomPasswordAndSendEmail(member);
        } else {
            member.updatePassword(member.getPassword(), passwordEncoder);
        }
    }

    public void createPositionByMember(Member member) {
        if (externalRetrievePositionUseCase.findByMemberIdAndYearAndPositionType(member.getId(), String.valueOf(LocalDate.now().getYear()), PositionType.MEMBER).isPresent()) {
            return;
        }
        Position position = Position.create(member.getId());
        externalRegisterPositionUseCase.save(position);
    }

    private void setRandomPasswordAndSendEmail(Member member) {
        String password = verificationService.generateVerificationCode();
        member.updatePassword(password, passwordEncoder);
        CompletableFuture.runAsync(() -> {
            try {
                emailService.broadcastEmailToApprovedMember(member, password);
            } catch (Exception e) {
                log.error("이메일 전송 실패: {}", e.getMessage());
            }
        });
    }
}
