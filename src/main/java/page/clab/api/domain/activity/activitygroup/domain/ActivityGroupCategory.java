package page.clab.api.domain.activity.activitygroup.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityGroupCategory {

    STUDY("STUDY", "스터디"),
    PROJECT("PROJECT", "프로젝트");

    private final String key;
    private final String description;
}
