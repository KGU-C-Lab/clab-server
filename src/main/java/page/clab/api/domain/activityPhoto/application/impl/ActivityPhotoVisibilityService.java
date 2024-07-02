package page.clab.api.domain.activityPhoto.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activityPhoto.application.ActivityPhotoVisibilityUseCase;
import page.clab.api.domain.activityPhoto.dao.ActivityPhotoRepository;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class ActivityPhotoVisibilityService implements ActivityPhotoVisibilityUseCase {

    private final ActivityPhotoRepository activityPhotoRepository;

    @Transactional
    @Override
    public Long update(Long activityPhotoId) {
        ActivityPhoto activityPhoto = getActivityPhotoByIdOrThrow(activityPhotoId);
        activityPhoto.togglePublicStatus();
        return activityPhotoRepository.save(activityPhoto).getId();
    }

    private ActivityPhoto getActivityPhotoByIdOrThrow(Long activityPhotoId) {
        return activityPhotoRepository.findById(activityPhotoId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 활동 사진입니다."));
    }
}