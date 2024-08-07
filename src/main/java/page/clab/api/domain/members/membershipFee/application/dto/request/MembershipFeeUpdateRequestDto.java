package page.clab.api.domain.members.membershipFee.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.members.membershipFee.domain.MembershipFeeStatus;

@Getter
@Setter
public class MembershipFeeUpdateRequestDto {

    @Schema(description = "카테고리", example = "지출")
    private String category;

    @Schema(description = "계좌", example = "110-123-456789")
    private String account;

    @Schema(description = "금액", example = "10000")
    private Long amount;

    @Schema(description = "내용", example = "2023-2 동아리 종강총회")
    private String content;

    @Schema(description = "증빙 사진", example = "https://images.chosun.com/resizer/mcbrEkwTr5YKQZ89QPO9hmdb0iE=/616x0/smart/cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/LPCZYYKZ4FFIJPDD344FSGCLCY.jpg")
    private String imageUrl;

    @Schema(description = "상태", example = "PENDING")
    private MembershipFeeStatus status;
}
