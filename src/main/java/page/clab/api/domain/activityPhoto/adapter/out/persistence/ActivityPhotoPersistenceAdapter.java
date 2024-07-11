package page.clab.api.domain.activityPhoto.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.activityPhoto.application.port.out.RegisterActivityPhotoPort;
import page.clab.api.domain.activityPhoto.application.port.out.RemoveActivityPhotoPort;
import page.clab.api.domain.activityPhoto.application.port.out.RetrieveActivityPhotoPort;
import page.clab.api.domain.activityPhoto.domain.ActivityPhoto;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ActivityPhotoPersistenceAdapter implements
        RegisterActivityPhotoPort,
        RetrieveActivityPhotoPort,
        RemoveActivityPhotoPort {

    private final ActivityPhotoRepository activityPhotoRepository;
    private final ActivityPhotoMapper activityPhotoMapper;

    @Override
    public ActivityPhoto save(ActivityPhoto activityPhoto) {
        ActivityPhotoJpaEntity entity = activityPhotoMapper.toJpaEntity(activityPhoto);
        ActivityPhotoJpaEntity savedEntity = activityPhotoRepository.save(entity);
        return activityPhotoMapper.toDomain(savedEntity);
    }

    @Override
    public void delete(ActivityPhoto activityPhoto) {
        ActivityPhotoJpaEntity entity = activityPhotoMapper.toJpaEntity(activityPhoto);
        activityPhotoRepository.delete(entity);
    }

    @Override
    public Optional<ActivityPhoto> findById(Long activityPhotoId) {
        return activityPhotoRepository.findById(activityPhotoId)
                .map(activityPhotoMapper::toDomain);
    }

    @Override
    public ActivityPhoto findByIdOrThrow(Long activityPhotoId) {
        return activityPhotoRepository.findById(activityPhotoId)
                .map(activityPhotoMapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[ActivityPhoto] id: " + activityPhotoId + "에 해당하는 활동 사진이 존재하지 않습니다."));
    }

    @Override
    public Page<ActivityPhoto> findByConditions(Boolean isPublic, Pageable pageable) {
        return activityPhotoRepository.findByConditions(isPublic, pageable)
                .map(activityPhotoMapper::toDomain);
    }
}
