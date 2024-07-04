package page.clab.api.domain.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.auth.util.AuthUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberRetrievalService implements RetrieveMemberUseCase {

    private final RetrieveMemberPort retrieveMemberPort;
    private final RetrieveMemberPort retrieveMemberPort1;

    @Override
    public Optional<Member> findById(String memberId) {
        return retrieveMemberPort.findById(memberId);
    }

    @Override
    public Member findByIdOrThrow(String memberId) {
        return retrieveMemberPort.findByIdOrThrow(memberId);
    }

    @Override
    public Member findByEmail(String email) {
        return retrieveMemberPort1.findByEmailOrThrow(email);
    }

    @Override
    public Member getCurrentMember() {
        String memberId = AuthUtil.getAuthenticationInfoMemberId();
        return retrieveMemberPort.findByIdOrThrow(memberId);
    }

    @Override
    public String getCurrentMemberId() {
        return AuthUtil.getAuthenticationInfoMemberId();
    }
}