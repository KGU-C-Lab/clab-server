package page.clab.api.domain.application.application;

import page.clab.api.domain.application.dto.response.ApplicationPassResponseDto;

public interface ApplicationPassCheckUseCase {
    ApplicationPassResponseDto checkPassStatus(Long recruitmentId, String studentId);
}