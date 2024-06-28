package page.clab.api.domain.accuse.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.accuse.dto.response.AccuseMyResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchMyAccusationsService {
    PagedResponseDto<AccuseMyResponseDto> execute(Pageable pageable);
}
