package page.clab.api.domain.activityPhoto.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activityPhoto.dao.ActivityPhotoRepository;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;
import page.clab.api.domain.activityPhoto.dto.response.ActivityPhotoResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class FetchActivityPhotosServiceImpl implements FetchActivityPhotosService {

    private final ActivityPhotoRepository activityPhotoRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<ActivityPhotoResponseDto> execute(Boolean isPublic, Pageable pageable) {
        Page<ActivityPhoto> activityPhotos = activityPhotoRepository.findByConditions(isPublic, pageable);
        return new PagedResponseDto<>(activityPhotos.map(ActivityPhotoResponseDto::toDto));
    }
}