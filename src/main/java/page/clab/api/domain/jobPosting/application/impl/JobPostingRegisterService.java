package page.clab.api.domain.jobPosting.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.jobPosting.application.JobPostingRegisterUseCase;
import page.clab.api.domain.jobPosting.dao.JobPostingRepository;
import page.clab.api.domain.jobPosting.domain.JobPosting;
import page.clab.api.domain.jobPosting.dto.request.JobPostingRequestDto;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class JobPostingRegisterService implements JobPostingRegisterUseCase {

    private final ValidationService validationService;
    private final JobPostingRepository jobPostingRepository;

    @Transactional
    @Override
    public Long register(JobPostingRequestDto requestDto) {
        JobPosting jobPosting = jobPostingRepository.findByJobPostingUrl(requestDto.getJobPostingUrl())
                .map(existingJobPosting -> existingJobPosting.updateFromRequestDto(requestDto))
                .orElseGet(() -> JobPostingRequestDto.toEntity(requestDto));
        validationService.checkValid(jobPosting);
        return jobPostingRepository.save(jobPosting).getId();
    }
}