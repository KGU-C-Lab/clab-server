package page.clab.api.domain.auth.login.application.port.in;

import page.clab.api.domain.auth.login.application.dto.response.TokenInfo;
import page.clab.api.domain.auth.login.domain.RedisToken;
import page.clab.api.domain.memberManagement.member.domain.Role;

import java.util.List;

public interface ManageRedisTokenUseCase {

    RedisToken findByAccessToken(String accessToken);

    RedisToken findByRefreshToken(String refreshToken);

    List<String> getCurrentLoggedInUsers();

    void saveToken(String memberId, Role role, TokenInfo tokenInfo, String ip);

    void deleteByAccessToken(String accessToken);
}
