package page.clab.api.domain.jobPosting.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.jobPosting.application.port.in.JobPostingUpdateUseCase;
import page.clab.api.domain.jobPosting.dto.request.JobPostingUpdateRequestDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/job-postings")
@RequiredArgsConstructor
@Tag(name = "JobPosting", description = "채용 공고")
public class JobPostingUpdateController {

    private final JobPostingUpdateUseCase jobPostingUpdateUseCase;

    @Operation(summary = "[A] 채용 공고 수정", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("/{jobPostingId}")
    public ApiResponse<Long> updateJobPosting(
            @PathVariable(name = "jobPostingId") Long jobPostingId,
            @Valid @RequestBody JobPostingUpdateRequestDto requestDto
    ) {
        Long id = jobPostingUpdateUseCase.update(jobPostingId, requestDto);
        return ApiResponse.success(id);
    }
}
