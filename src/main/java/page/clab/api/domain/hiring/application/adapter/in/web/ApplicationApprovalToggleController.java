package page.clab.api.domain.hiring.application.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.hiring.application.application.port.in.ToggleApplicationApprovalUseCase;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
@Tag(name = "Hiring - Application", description = "동아리 지원")
public class ApplicationApprovalToggleController {

    private final ToggleApplicationApprovalUseCase toggleApplicationApprovalUseCase;

    @Operation(summary = "[S] 지원 합격/취소", description = "ROLE_SUPER 이상의 권한이 필요함<br>" +
            "승인/취소 상태가 반전됨")
    @Secured({ "ROLE_SUPER" })
    @PatchMapping("/{recruitmentId}/{studentId}")
    public ApiResponse<String> toggleApprovalStatus(
            @PathVariable(name = "recruitmentId") Long recruitmentId,
            @PathVariable(name = "studentId") String studentId
    ) {
        String id = toggleApplicationApprovalUseCase.toggleApprovalStatus(recruitmentId, studentId);
        return ApiResponse.success(id);
    }
}
