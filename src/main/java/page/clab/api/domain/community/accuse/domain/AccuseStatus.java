package page.clab.api.domain.community.accuse.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccuseStatus {

    PENDING("PENDING", "처리 중"),
    APPROVED("APPROVED", "처리 완료"),
    REJECTED("REJECTED", "거부");

    private final String key;
    private final String description;
}
