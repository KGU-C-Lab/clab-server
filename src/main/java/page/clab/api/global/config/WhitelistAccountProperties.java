package page.clab.api.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "security.account.whitelist-admin")
public class WhitelistAccountProperties {

    private String username;
    private String password;
    private String role;
}
