package page.clab.api.domain.workExperience.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.workExperience.application.port.in.DeletedWorkExperiencesRetrievalUseCase;
import page.clab.api.domain.workExperience.application.port.out.RetrieveDeletedWorkExperiencePort;
import page.clab.api.domain.workExperience.domain.WorkExperience;
import page.clab.api.domain.workExperience.dto.response.WorkExperienceResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedWorkExperiencesRetrievalService implements DeletedWorkExperiencesRetrievalUseCase {

    private final RetrieveDeletedWorkExperiencePort retrieveDeletedWorkExperiencePort;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<WorkExperienceResponseDto> retrieve(Pageable pageable) {
        Page<WorkExperience> workExperiences = retrieveDeletedWorkExperiencePort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(workExperiences.map(WorkExperienceResponseDto::toDto));
    }
}