package page.clab.api.domain.membershipFee.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.membershipFee.dao.MembershipFeeRepository;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.domain.membershipFee.dto.response.MembershipFeeResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class FetchDeletedMembershipFeesServiceImpl implements FetchDeletedMembershipFeesService {

    private final MemberLookupService memberLookupService;
    private final MembershipFeeRepository membershipFeeRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<MembershipFeeResponseDto> execute(Pageable pageable) {
        boolean isAdminRole = memberLookupService.getCurrentMemberDetailedInfo().isAdminRole();
        Page<MembershipFee> membershipFees = membershipFeeRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(membershipFees.map(membershipFee -> {
            String applicantName = memberLookupService.getMemberBasicInfoById(membershipFee.getMemberId()).getMemberName();
            return MembershipFeeResponseDto.toDto(membershipFee, applicantName, isAdminRole);
        }));
    }
}