package page.clab.api.domain.login.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.login.application.LoginService;
import page.clab.api.domain.login.dto.request.LoginRequestDto;
import page.clab.api.domain.login.dto.request.TwoFactorAuthenticationRequestDto;
import page.clab.api.domain.login.dto.response.LoginResult;
import page.clab.api.domain.login.dto.response.TokenHeader;
import page.clab.api.domain.login.exception.LoginFaliedException;
import page.clab.api.domain.login.exception.MemberLockedException;
import page.clab.api.global.common.dto.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
@Tag(name = "Login", description = "로그인")
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @Value("${security.auth.header}")
    private String authHeader;

    @Operation(summary = "멤버 로그인", description = "ROLE_ANONYMOUS 권한이 필요함")
    @PostMapping("")
    public ApiResponse<Boolean> login(
            HttpServletRequest request,
            HttpServletResponse response,
            @Valid @RequestBody LoginRequestDto requestDto
    ) throws MemberLockedException, LoginFaliedException {
        LoginResult result = loginService.login(request, requestDto);
        response.setHeader(authHeader, result.getHeader());
        return ApiResponse.success(result.getBody());
    }

    @Operation(summary = "TOTP 인증", description = "ROLE_ANONYMOUS 권한이 필요함")
    @PostMapping("/authenticator")
    public ApiResponse<Boolean> authenticator(
            HttpServletRequest request,
            HttpServletResponse response,
            @Valid @RequestBody TwoFactorAuthenticationRequestDto requestDto
    ) throws LoginFaliedException, MemberLockedException {
        LoginResult result = loginService.authenticator(request, requestDto);
        response.setHeader(authHeader, result.getHeader());
        return ApiResponse.success(result.getBody());
    }

    @Operation(summary = "[S] TOTP 초기화", description = "ROLE_SUPER 권한이 필요함")
    @DeleteMapping("/authenticator/{memberId}")
    @Secured({"ROLE_SUPER"})
    public ApiResponse<String> deleteAuthenticator(
            @PathVariable(name = "memberId") String memberId
    ) {
        String id = loginService.resetAuthenticator(memberId);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[S] 멤버 토큰 삭제", description = "ROLE_SUPER 이상의 권한이 필요함")
    @DeleteMapping("/revoke/{memberId}")
    @Secured({"ROLE_SUPER"})
    public ApiResponse<String> revoke(
            @PathVariable(name = "memberId") String memberId
    ) {
        String id = loginService.revoke(memberId);
        return ApiResponse.success(id);
    }

    @Operation(summary = "[U] 멤버 토큰 재발급", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("/reissue")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    public ApiResponse reissue(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        TokenHeader headerData = loginService.reissue(request);
        response.setHeader(authHeader, headerData.toJson());
        return ApiResponse.success();
    }

    @Operation(summary = "[S] 현재 로그인 중인 멤버 조회", description = "ROLE_SUPER 이상의 권한이 필요함<br>" +
            "Redis에 저장된 토큰을 조회하여 현재 로그인 중인 멤버를 조회합니다.")
    @GetMapping("/current")
    @Secured({"ROLE_SUPER"})
    public ApiResponse<List<String>> getCurrentLoggedInUsers() {
        List<String> currentLoggedInUsers = loginService.getCurrentLoggedInUsers();
        return ApiResponse.success(currentLoggedInUsers);
    }

}
