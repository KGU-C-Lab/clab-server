package page.clab.api.domain.executives.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExecutivesPosition {

    PRESIDENT("PRESIDENT", "회장"),
    VICE_PRESIDENT("VICE_PRESIDENT", "부회장"),
    OPERATIONS("OPERATIONS", "운영진");

    @Enumerated(EnumType.STRING)
    private String key;
    private String description;

}
