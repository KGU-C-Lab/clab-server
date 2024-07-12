package page.clab.api.domain.board.application.service;

import com.drew.lang.annotations.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.port.in.RetrieveBoardsUseCase;
import page.clab.api.domain.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.dto.response.BoardListResponseDto;
import page.clab.api.domain.comment.application.port.out.RetrieveCommentPort;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class BoardsRetrievalService implements RetrieveBoardsUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrieveBoardPort retrieveBoardPort;
    private final RetrieveCommentPort retrieveCommentPort;

    @Transactional
    @Override
    public PagedResponseDto<BoardListResponseDto> retrieveBoards(Pageable pageable) {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        Page<Board> boards = retrieveBoardPort.findAll(pageable);
        return new PagedResponseDto<>(boards.map(board -> mapToBoardListResponseDto(board, currentMemberInfo)));
    }

    @Override
    public Board findByIdOrThrow(Long boardId) {
        return retrieveBoardPort.findByIdOrThrow(boardId);
    }

    @NotNull
    private BoardListResponseDto mapToBoardListResponseDto(Board board, MemberDetailedInfoDto memberInfo) {
        Long commentCount = retrieveCommentPort.countByBoard(board);
        return BoardListResponseDto.toDto(board, memberInfo, commentCount);
    }
}