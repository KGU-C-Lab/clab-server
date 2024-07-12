package page.clab.api.domain.recruitment.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.recruitment.dto.response.RecruitmentResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveDeletedRecruitmentsUseCase {
    PagedResponseDto<RecruitmentResponseDto> retrieveDeletedRecruitments(Pageable pageable);
}