package page.clab.api.domain.members.schedule.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SchedulePriority {

    HIGH("HIGH", "높음"),
    MEDIUM("MEDIUM", "중간"),
    LOW("LOW", "낮음");

    private final String key;
    private final String description;
}
