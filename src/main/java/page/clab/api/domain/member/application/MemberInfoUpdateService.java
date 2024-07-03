package page.clab.api.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.MemberInfoUpdateUseCase;
import page.clab.api.domain.member.application.port.in.MemberRetrievalUseCase;
import page.clab.api.domain.member.application.port.out.LoadMemberPort;
import page.clab.api.domain.member.application.port.out.UpdateMemberPort;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.request.MemberUpdateRequestDto;
import page.clab.api.domain.member.event.MemberUpdatedEvent;
import page.clab.api.global.common.file.application.FileService;
import page.clab.api.global.common.file.dto.request.DeleteFileRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class MemberInfoUpdateService implements MemberInfoUpdateUseCase {

    private final MemberRetrievalUseCase memberRetrievalUseCase;
    private final LoadMemberPort loadMemberPort;
    private final UpdateMemberPort updateMemberPort;
    private final ValidationService validationService;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public String update(String memberId, MemberUpdateRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = memberRetrievalUseCase.getCurrentMember();
        Member member = loadMemberPort.findByIdOrThrow(memberId);
        member.validateAccessPermission(currentMember);
        updateMember(requestDto, member);
        validationService.checkValid(member);
        updateMemberPort.update(member);
        eventPublisher.publishEvent(new MemberUpdatedEvent(this, member.getId()));
        return member.getId();
    }

    private void updateMember(MemberUpdateRequestDto requestDto, Member member) throws PermissionDeniedException {
        String previousImageUrl = member.getImageUrl();
        member.update(requestDto, passwordEncoder);
        if (requestDto.getImageUrl() != null && requestDto.getImageUrl().isEmpty()) {
            member.clearImageUrl();
            fileService.deleteFile(DeleteFileRequestDto.create(previousImageUrl));
        }
    }
}
