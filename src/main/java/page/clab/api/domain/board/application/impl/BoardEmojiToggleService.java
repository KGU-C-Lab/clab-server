package page.clab.api.domain.board.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.BoardEmojiToggleUseCase;
import page.clab.api.domain.board.application.BoardLookupUseCase;
import page.clab.api.domain.board.dao.BoardEmojiRepository;
import page.clab.api.domain.board.dao.BoardRepository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.BoardEmoji;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.InvalidEmojiException;
import page.clab.api.global.util.EmojiUtils;

@Service
@RequiredArgsConstructor
public class BoardEmojiToggleService implements BoardEmojiToggleUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final BoardLookupUseCase boardLookupUseCase;
    private final BoardRepository boardRepository;
    private final BoardEmojiRepository boardEmojiRepository;

    @Transactional
    @Override
    public String toggleEmojiStatus(Long boardId, String emoji) {
        if (!EmojiUtils.isEmoji(emoji)) {
            throw new InvalidEmojiException("지원하지 않는 이모지입니다.");
        }
        MemberDetailedInfoDto currentMemberInfo = memberLookupUseCase.getCurrentMemberDetailedInfo();
        String memberId = currentMemberInfo.getMemberId();
        Board board = boardLookupUseCase.getBoardByIdOrThrow(boardId);
        BoardEmoji boardEmoji = boardEmojiRepository.findByBoardIdAndMemberIdAndEmoji(boardId, memberId, emoji)
                .map(existingEmoji -> {
                    existingEmoji.toggleIsDeletedStatus();
                    return existingEmoji;
                })
                .orElseGet(() -> BoardEmoji.create(memberId, boardId, emoji));
        boardEmojiRepository.save(boardEmoji);
        return board.getCategory().getKey();
    }
}