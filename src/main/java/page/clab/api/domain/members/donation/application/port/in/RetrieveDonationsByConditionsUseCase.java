package page.clab.api.domain.members.donation.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.members.donation.application.dto.response.DonationResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.time.LocalDate;

public interface RetrieveDonationsByConditionsUseCase {
    PagedResponseDto<DonationResponseDto> retrieveDonations(String memberId, String name, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
