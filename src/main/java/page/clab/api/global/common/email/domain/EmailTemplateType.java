package page.clab.api.global.common.email.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailTemplateType {

    NORMAL("NORMAL", "기본", "clabEmail.html");

    @Enumerated(EnumType.STRING)
    private String key;
    private String description;
    private String templateName;

}
