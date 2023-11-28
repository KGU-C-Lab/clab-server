package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.ActivityPhoto;
import page.clab.api.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityPhotoResponseDto {

    private Long id;
    
    private String imageUrl;

    private Boolean isPublic;

    private String createdAt;

    public static ActivityPhotoResponseDto of(ActivityPhoto activityPhoto) {
        return ModelMapperUtil.getModelMapper().map(activityPhoto, ActivityPhotoResponseDto.class);
    }

}
