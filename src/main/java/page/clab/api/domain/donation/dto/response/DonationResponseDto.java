package page.clab.api.domain.donation.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.donation.domain.Donation;

import java.time.LocalDateTime;

@Getter
@Builder
public class DonationResponseDto {

    private Long id;

    private String donorId;

    private String name;

    private Double amount;

    private String message;

    private LocalDateTime createdAt;

    public static DonationResponseDto toDto(Donation donation) {
        return DonationResponseDto.builder()
                .id(donation.getId())
                .donorId(donation.getDonor().getId())
                .name(donation.getDonor().getName())
                .amount(donation.getAmount())
                .message(donation.getMessage())
                .createdAt(donation.getCreatedAt())
                .build();
    }

}
