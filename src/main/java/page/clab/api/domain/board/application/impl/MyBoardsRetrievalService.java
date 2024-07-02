package page.clab.api.domain.board.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.MyBoardsRetrievalUseCase;
import page.clab.api.domain.board.dao.BoardRepository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.dto.response.BoardMyResponseDto;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberBasicInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MyBoardsRetrievalService implements MyBoardsRetrievalUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final BoardRepository boardRepository;

    @Transactional
    @Override
    public PagedResponseDto<BoardMyResponseDto> retrieve(Pageable pageable) {
        MemberBasicInfoDto currentMemberInfo = memberLookupUseCase.getCurrentMemberBasicInfo();
        Page<Board> boards = boardRepository.findAllByMemberId(currentMemberInfo.getMemberId(), pageable);
        return new PagedResponseDto<>(boards.map(board -> BoardMyResponseDto.toDto(board, currentMemberInfo)));
    }
}