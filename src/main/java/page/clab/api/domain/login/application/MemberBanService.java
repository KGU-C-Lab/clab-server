package page.clab.api.domain.login.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.application.port.in.MemberBanUseCase;
import page.clab.api.domain.login.application.port.in.RedisTokenManagementUseCase;
import page.clab.api.domain.login.application.port.out.LoadAccountLockInfoPort;
import page.clab.api.domain.login.application.port.out.RegisterAccountLockInfoPort;
import page.clab.api.domain.login.domain.AccountLockInfo;
import page.clab.api.domain.member.application.port.in.MemberInfoRetrievalUseCase;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class MemberBanService implements MemberBanUseCase {

    private final MemberInfoRetrievalUseCase memberInfoRetrievalUseCase;
    private final RedisTokenManagementUseCase redisTokenManagementUseCase;
    private final SlackService slackService;
    private final LoadAccountLockInfoPort loadAccountLockInfoPort;
    private final RegisterAccountLockInfoPort registerAccountLockInfoPort;

    @Transactional
    @Override
    public Long ban(HttpServletRequest request, String memberId) {
        MemberBasicInfoDto memberInfo = memberInfoRetrievalUseCase.getMemberBasicInfoById(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(memberInfo.getMemberId());
        accountLockInfo.banPermanently();
        redisTokenManagementUseCase.deleteByMemberId(memberId);
        sendSlackBanNotification(request, memberId);
        return registerAccountLockInfoPort.save(accountLockInfo).getId();
    }

    private AccountLockInfo ensureAccountLockInfo(String memberId) {
        return loadAccountLockInfoPort.findByMemberId(memberId)
                .orElseGet(() -> createAccountLockInfo(memberId));
    }

    private AccountLockInfo createAccountLockInfo(String memberId) {
        AccountLockInfo accountLockInfo = AccountLockInfo.create(memberId);
        registerAccountLockInfoPort.save(accountLockInfo);
        return accountLockInfo;
    }

    private void sendSlackBanNotification(HttpServletRequest request, String memberId) {
        String memberName = memberInfoRetrievalUseCase.getMemberBasicInfoById(memberId).getMemberName();
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.MEMBER_BANNED, "ID: " + memberId + ", Name: " + memberName);
    }
}