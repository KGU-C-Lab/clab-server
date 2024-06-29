package page.clab.api.domain.membershipFee.application;

import page.clab.api.domain.membershipFee.dto.request.MembershipFeeUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

public interface UpdateMembershipFeeService {
    Long execute(Long membershipFeeId, MembershipFeeUpdateRequestDto requestDto) throws PermissionDeniedException;
}