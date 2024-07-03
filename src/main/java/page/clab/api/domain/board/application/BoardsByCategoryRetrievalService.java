package page.clab.api.domain.board.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.port.in.BoardsByCategoryRetrievalUseCase;
import page.clab.api.domain.board.application.port.out.RetrieveBoardsByCategoryPort;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.BoardCategory;
import page.clab.api.domain.board.dto.response.BoardCategoryResponseDto;
import page.clab.api.domain.member.application.port.in.MemberInfoRetrievalUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class BoardsByCategoryRetrievalService implements BoardsByCategoryRetrievalUseCase {

    private final MemberInfoRetrievalUseCase memberInfoRetrievalUseCase;
    private final RetrieveBoardsByCategoryPort retrieveBoardsByCategoryPort;

    @Transactional
    @Override
    public PagedResponseDto<BoardCategoryResponseDto> retrieve(BoardCategory category, Pageable pageable) {
        MemberDetailedInfoDto currentMemberInfo = memberInfoRetrievalUseCase.getCurrentMemberDetailedInfo();
        Page<Board> boards = retrieveBoardsByCategoryPort.findAllByCategory(category, pageable);
        return new PagedResponseDto<>(boards.map(board -> BoardCategoryResponseDto.toDto(board, currentMemberInfo, 0L))); // Update the comment count accordingly
    }
}
