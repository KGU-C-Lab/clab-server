package page.clab.api.domain.recruitment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.recruitment.application.DeleteRecruitmentService;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/recruitments")
@RequiredArgsConstructor
@Tag(name = "Recruitment", description = "모집 공고")
public class DeleteRecruitmentController {

    private final DeleteRecruitmentService deleteRecruitmentService;

    @Operation(summary = "[S] 모집 공고 삭제", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @DeleteMapping("/{recruitmentId}")
    public ApiResponse<Long> deleteRecruitment(
            @PathVariable(name = "recruitmentId") Long recruitmentId
    ) {
        Long id = deleteRecruitmentService.execute(recruitmentId);
        return ApiResponse.success(id);
    }
}