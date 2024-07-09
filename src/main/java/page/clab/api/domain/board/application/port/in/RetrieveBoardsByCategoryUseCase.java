package page.clab.api.domain.board.application.port.in;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.board.domain.BoardCategory;
import page.clab.api.domain.board.dto.response.BoardCategoryResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface RetrieveBoardsByCategoryUseCase {
    PagedResponseDto<BoardCategoryResponseDto> retrieveBoardsByCategory(BoardCategory category, Pageable pageable);
}
