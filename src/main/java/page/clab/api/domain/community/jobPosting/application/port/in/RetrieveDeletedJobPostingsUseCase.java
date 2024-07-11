package page.clab.api.domain.community.jobPosting.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.jobPosting.application.dto.response.JobPostingDetailsResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveDeletedJobPostingsUseCase {
    PagedResponseDto<JobPostingDetailsResponseDto> retrieveDeletedJobPostings(Pageable pageable);
}