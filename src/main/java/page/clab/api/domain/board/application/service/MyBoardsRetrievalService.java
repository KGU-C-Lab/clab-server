package page.clab.api.domain.board.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.port.in.RetrieveMyBoardsUseCase;
import page.clab.api.domain.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.dto.response.BoardMyResponseDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MyBoardsRetrievalService implements RetrieveMyBoardsUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrieveBoardPort retrieveBoardPort;

    @Transactional
    @Override
    public PagedResponseDto<BoardMyResponseDto> retrieveMyBoards(Pageable pageable) {
        MemberBasicInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberBasicInfo();
        Page<Board> boards = retrieveBoardPort.findAllByMemberId(currentMemberInfo.getMemberId(), pageable);
        return new PagedResponseDto<>(boards.map(board -> BoardMyResponseDto.toDto(board, currentMemberInfo)));
    }
}