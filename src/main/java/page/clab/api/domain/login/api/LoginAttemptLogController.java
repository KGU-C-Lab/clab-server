package page.clab.api.domain.login.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.login.application.LoginAttemptLogService;
import page.clab.api.domain.login.dto.response.LoginAttemptLogResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;

@RestController
@RequestMapping("/api/v1/login-attempt-logs")
@RequiredArgsConstructor
@Tag(name = "LoginAttemptLog", description = "로그인 시도 로그")
@Slf4j
public class LoginAttemptLogController {

    private final LoginAttemptLogService loginAttemptLogService;

    @Operation(summary = "[S] 계정별 로그인 시도 로그 조회", description = "ROLE_SUPER 이상의 권한이 필요함")
    @Secured({"ROLE_SUPER"})
    @GetMapping("/{memberId}")
    public ResponseModel getLoginAttemptLogs(
            @PathVariable(name = "memberId") String memberId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<LoginAttemptLogResponseDto> loginAttemptLogs = loginAttemptLogService.getLoginAttemptLogs(memberId, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(loginAttemptLogs);
        return responseModel;
    }

}
