package page.clab.api.domain.activityGroup.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Embeddable
public class GroupMemberId implements Serializable {

    @EqualsAndHashCode.Include
    private String member;

    @EqualsAndHashCode.Include
    private Long activityGroup;

}