package page.clab.api.domain.schedule.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.schedule.application.port.in.RemoveScheduleUseCase;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.exception.PermissionDeniedException;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedule", description = "일정")
public class ScheduleRemoveController {

    private final RemoveScheduleUseCase removeScheduleUseCase;

    @Operation(summary = "[U] 일정 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER" })
    @DeleteMapping("/{scheduleId}")
    public ApiResponse<Long> removeSchedule(
            @PathVariable(name = "scheduleId") Long scheduleId
    ) throws PermissionDeniedException {
        Long id = removeScheduleUseCase.removeSchedule(scheduleId);
        return ApiResponse.success(id);
    }
}
