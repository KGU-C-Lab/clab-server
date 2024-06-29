package page.clab.api.domain.donation.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.donation.application.FetchDeletedDonationsService;
import page.clab.api.domain.donation.dao.DonationRepository;
import page.clab.api.domain.donation.domain.Donation;
import page.clab.api.domain.donation.dto.response.DonationResponseDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class FetchDeletedDonationsServiceImpl implements FetchDeletedDonationsService {

    private final DonationRepository donationRepository;
    private final MemberLookupService memberLookupService;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<DonationResponseDto> execute(Pageable pageable) {
        Page<Donation> donations = donationRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(donations.map(donation -> {
            MemberBasicInfoDto memberInfo = memberLookupService.getMemberBasicInfoById(donation.getMemberId());
            return DonationResponseDto.toDto(donation, memberInfo.getMemberName());
        }));
    }
}