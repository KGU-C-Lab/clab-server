package page.clab.api.domain.workExperience.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.workExperience.application.port.in.WorkExperienceRegisterUseCase;
import page.clab.api.domain.workExperience.dto.request.WorkExperienceRequestDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/work-experiences")
@RequiredArgsConstructor
@Tag(name = "WorkExperience", description = "경력사항")
public class WorkExperienceRegisterController {

    private final WorkExperienceRegisterUseCase workExperienceRegisterUseCase;

    @Operation(summary = "[U] 경력사항 등록", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ApiResponse<Long> registerWorkExperience(
            @Valid @RequestBody WorkExperienceRequestDto requestDto
    ) {
        Long id = workExperienceRegisterUseCase.register(requestDto);
        return ApiResponse.success(id);
    }
}