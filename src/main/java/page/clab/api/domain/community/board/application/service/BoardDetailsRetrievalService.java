package page.clab.api.domain.community.board.application.service;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.board.application.dto.response.BoardDetailsResponseDto;
import page.clab.api.domain.community.board.application.dto.response.BoardEmojiCountResponseDto;
import page.clab.api.domain.community.board.application.port.in.RetrieveBoardDetailsUseCase;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardEmojiPort;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberInfoUseCase;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardDetailsRetrievalService implements RetrieveBoardDetailsUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrieveBoardPort retrieveBoardPort;
    private final RetrieveBoardEmojiPort retrieveBoardEmojiPort;

    @Transactional
    @Override
    public BoardDetailsResponseDto retrieveBoardDetails(Long boardId) {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        Board board = retrieveBoardPort.findByIdOrThrow(boardId);
        boolean isOwner = board.isOwner(currentMemberInfo.getMemberId());
        List<BoardEmojiCountResponseDto> emojiInfos = getBoardEmojiCountResponseDtoList(boardId, currentMemberInfo.getMemberId());
        return BoardDetailsResponseDto.toDto(board, currentMemberInfo, isOwner, emojiInfos);
    }

    @Transactional(readOnly = true)
    public List<BoardEmojiCountResponseDto> getBoardEmojiCountResponseDtoList(Long boardId, String memberId) {
        List<Tuple> results = retrieveBoardEmojiPort.findEmojiClickCountsByBoardId(boardId, memberId);
        return results.stream()
                .map(result -> new BoardEmojiCountResponseDto(
                        result.get("emoji", String.class),
                        result.get("count", Long.class),
                        result.get("isClicked", Boolean.class)))
                .collect(Collectors.toList());
    }
}
