package page.clab.api.domain.login.application.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.login.application.BanMemberService;
import page.clab.api.domain.login.application.RedisTokenService;
import page.clab.api.domain.login.dao.AccountLockInfoRepository;
import page.clab.api.domain.login.domain.AccountLockInfo;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.common.slack.domain.SecurityAlertType;

@Service
@RequiredArgsConstructor
public class BanMemberServiceImpl implements BanMemberService {

    private final MemberLookupService memberLookupService;
    private final RedisTokenService redisTokenService;
    private final SlackService slackService;
    private final AccountLockInfoRepository accountLockInfoRepository;

    @Transactional
    @Override
    public Long execute(HttpServletRequest request, String memberId) {
        MemberBasicInfoDto memberInfo = memberLookupService.getMemberBasicInfoById(memberId);
        AccountLockInfo accountLockInfo = ensureAccountLockInfo(memberInfo.getMemberId());
        accountLockInfo.banPermanently();
        redisTokenService.deleteRedisTokenByMemberId(memberId);
        sendSlackBanNotification(request, memberId);
        return accountLockInfoRepository.save(accountLockInfo).getId();
    }

    private AccountLockInfo ensureAccountLockInfo(String memberId) {
        return accountLockInfoRepository.findByMemberId(memberId)
                .orElseGet(() -> createAccountLockInfo(memberId));
    }

    private AccountLockInfo createAccountLockInfo(String memberId) {
        AccountLockInfo accountLockInfo = AccountLockInfo.create(memberId);
        accountLockInfoRepository.save(accountLockInfo);
        return accountLockInfo;
    }

    private void sendSlackBanNotification(HttpServletRequest request, String memberId) {
        String memberName = memberLookupService.getMemberBasicInfoById(memberId).getMemberName();
        slackService.sendSecurityAlertNotification(request, SecurityAlertType.MEMBER_BANNED, "ID: " + memberId + ", Name: " + memberName);
    }
}