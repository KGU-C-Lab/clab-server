package page.clab.api.domain.login.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.login.application.dto.response.AccountAccessLogResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveAccountAccessLogsUseCase {
    PagedResponseDto<AccountAccessLogResponseDto> retrieveAccountAccessLogs(String memberId, Pageable pageable);
}