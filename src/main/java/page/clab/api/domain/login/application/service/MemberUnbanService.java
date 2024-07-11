package page.clab.api.domain.login.application.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.application.port.in.UnbanMemberUseCase;
import page.clab.api.domain.login.application.port.out.RegisterAccountLockInfoPort;
import page.clab.api.domain.login.application.port.out.RetrieveAccountLockInfoPort;
import page.clab.api.domain.login.domain.AccountLockInfo;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberBasicInfoDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class MemberUnbanService implements UnbanMemberUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final SlackService slackService;
    private final RetrieveAccountLockInfoPort retrieveAccountLockInfoPort;
    private final RegisterAccountLockInfoPort registerAccountLockInfoPort;

    @Transactional
    @Override
    public Long unbanMember(HttpServletRequest request, String memberId) {
        MemberBasicInfoDto memberInfo = retrieveMemberInfoUseCase.getMemberBasicInfoById(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(memberInfo.getMemberId());
        accountLockInfo.unban();
        sendSlackUnbanNotification(request, memberId);
        return registerAccountLockInfoPort.save(accountLockInfo).getId();
    }

    private AccountLockInfo ensureAccountLockInfo(String memberId) {
        return retrieveAccountLockInfoPort.findByMemberId(memberId)
                .orElseGet(() -> createAccountLockInfo(memberId));
    }

    private AccountLockInfo createAccountLockInfo(String memberId) {
        AccountLockInfo accountLockInfo = AccountLockInfo.create(memberId);
        registerAccountLockInfoPort.save(accountLockInfo);
        return accountLockInfo;
    }

    private void sendSlackUnbanNotification(HttpServletRequest request, String memberId) {
        String memberName = retrieveMemberInfoUseCase.getMemberBasicInfoById(memberId).getMemberName();
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.MEMBER_UNBANNED, "ID: " + memberId + ", Name: " + memberName);
    }
}
