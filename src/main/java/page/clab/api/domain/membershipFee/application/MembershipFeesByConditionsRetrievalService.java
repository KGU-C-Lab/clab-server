package page.clab.api.domain.membershipFee.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.MemberInfoRetrievalUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.membershipFee.application.port.in.MembershipFeesByConditionsRetrievalUseCase;
import page.clab.api.domain.membershipFee.application.port.out.RetrieveMembershipFeesByConditionsPort;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.domain.membershipFee.domain.MembershipFeeStatus;
import page.clab.api.domain.membershipFee.dto.response.MembershipFeeResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MembershipFeesByConditionsRetrievalService implements MembershipFeesByConditionsRetrievalUseCase {

    private final MemberInfoRetrievalUseCase memberInfoRetrievalUseCase;
    private final RetrieveMembershipFeesByConditionsPort retrieveMembershipFeesByConditionsPort;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<MembershipFeeResponseDto> retrieve(String memberId, String memberName, String category, MembershipFeeStatus status, Pageable pageable) {
        MemberDetailedInfoDto memberInfo = memberInfoRetrievalUseCase.getCurrentMemberDetailedInfo();
        Page<MembershipFee> membershipFeesPage = retrieveMembershipFeesByConditionsPort.findByConditions(memberId, memberName, category, status, pageable);
        return new PagedResponseDto<>(membershipFeesPage.map(membershipFee ->
                MembershipFeeResponseDto.toDto(membershipFee, memberInfo.getMemberName(), memberInfo.isAdminRole())));
    }
}
