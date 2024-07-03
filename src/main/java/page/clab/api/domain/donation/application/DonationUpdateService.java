package page.clab.api.domain.donation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.donation.application.port.in.DonationUpdateUseCase;
import page.clab.api.domain.donation.application.port.out.LoadDonationPort;
import page.clab.api.domain.donation.application.port.out.RegisterDonationPort;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.donation.dto.request.DonationUpdateRequestDto;
import page.clab.api.domain.member.application.port.in.MemberInfoRetrievalUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class DonationUpdateService implements DonationUpdateUseCase {

    private final LoadDonationPort loadDonationPort;
    private final RegisterDonationPort registerDonationPort;
    private final MemberInfoRetrievalUseCase memberInfoRetrievalUseCase;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long update(Long donationId, DonationUpdateRequestDto donationUpdateRequestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberInfoRetrievalUseCase.getCurrentMemberDetailedInfo();
        Donation donation = loadDonationPort.findByIdOrThrow(donationId);
        donation.validateAccessPermission(currentMemberInfo.isSuperAdminRole());
        donation.update(donationUpdateRequestDto);
        validationService.checkValid(donation);
        return registerDonationPort.save(donation).getId();
    }
}
