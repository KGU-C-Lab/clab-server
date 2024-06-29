package page.clab.api.domain.workExperience.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.workExperience.dto.response.WorkExperienceResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchWorkExperiencesByConditionsService {
    PagedResponseDto<WorkExperienceResponseDto> fetchWorkExperiencesByConditions(String memberId, Pageable pageable);
}