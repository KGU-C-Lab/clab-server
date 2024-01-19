package page.clab.api.domain.membershipFee.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.membershipFee.domain.MembershipFee;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MembershipFeeResponseDto {

    private Long id;

    private String memberId;

    private String memberName;

    private String category;

    private String content;

    private String imageUrl;

    private LocalDateTime createdAt;

    public static MembershipFeeResponseDto of(MembershipFee membershipFee) {
        MembershipFeeResponseDto membershipFeeResponseDto = ModelMapperUtil.getModelMapper()
                .map(membershipFee, MembershipFeeResponseDto.class);
        membershipFeeResponseDto.setMemberId(membershipFee.getApplicant().getId());
        membershipFeeResponseDto.setMemberName(membershipFee.getApplicant().getName());
        return membershipFeeResponseDto;
    }

}