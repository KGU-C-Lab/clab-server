package page.clab.api.domain.activityGroup.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.dto.request.ApplyFormRequestDto;
import page.clab.api.domain.member.domain.Member;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "activity_group_id", nullable = false)
    private ActivityGroup activityGroup;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotNull
    private String contact;

    @NotNull
    private String email;

    @NotNull
    private String applyReason;

    @NotNull
    private String spec;

    public static ApplyForm of(ApplyFormRequestDto requestDto){
        return ApplyForm.builder()
                .contact(requestDto.getContact())
                .email(requestDto.getEmail())
                .applyReason(requestDto.getApplyReason())
                .spec(requestDto.getSpec())
                .build();
    }

}