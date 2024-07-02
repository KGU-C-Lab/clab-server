package page.clab.api.domain.application.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.application.dto.response.ApplicationResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface DeletedApplicationRetrievalUseCase {
    PagedResponseDto<ApplicationResponseDto> retrieve(Pageable pageable);
}