package page.clab.api.domain.accuse.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.TargetType;

import java.time.LocalDateTime;

@Getter
@Builder
public class AccuseMyResponseDto {

    private TargetType targetType;

    private Long targetId;

    private String reason;

    private AccuseStatus accuseStatus;

    private LocalDateTime createdAt;

    public static AccuseMyResponseDto toDto(Accuse accuse) {
        return AccuseMyResponseDto.builder()
                .targetType(accuse.getTarget().getTargetType())
                .targetId(accuse.getTarget().getTargetReferenceId())
                .reason(accuse.getReason())
                .accuseStatus(accuse.getTarget().getAccuseStatus())
                .createdAt(accuse.getTarget().getCreatedAt())
                .build();
    }

}
