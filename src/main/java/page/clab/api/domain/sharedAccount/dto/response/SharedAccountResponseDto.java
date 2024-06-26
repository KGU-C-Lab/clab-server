package page.clab.api.domain.sharedAccount.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.sharedAccount.domain.SharedAccount;

@Getter
@Builder
public class SharedAccountResponseDto {

    private Long id;

    private String username;

    private String password;

    private String platformName;

    private String platformUrl;

    private boolean isInUse;

    public static SharedAccountResponseDto toDto(SharedAccount sharedAccount) {
        return SharedAccountResponseDto.builder()
                .id(sharedAccount.getId())
                .username(sharedAccount.getUsername())
                .password(sharedAccount.getPassword())
                .platformName(sharedAccount.getPlatformName())
                .platformUrl(sharedAccount.getPlatformUrl())
                .isInUse(sharedAccount.isInUse())
                .build();
    }

}
