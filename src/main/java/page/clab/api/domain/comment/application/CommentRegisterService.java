package page.clab.api.domain.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.port.in.BoardLookupUseCase;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.comment.application.port.in.CommentRegisterUseCase;
import page.clab.api.domain.comment.application.port.out.LoadCommentPort;
import page.clab.api.domain.comment.application.port.out.RegisterCommentPort;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.comment.dto.request.CommentRequestDto;
import page.clab.api.domain.member.application.port.in.MemberRetrievalUseCase;
import page.clab.api.domain.notification.application.port.in.NotificationSenderUseCase;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class CommentRegisterService implements CommentRegisterUseCase {

    private final BoardLookupUseCase boardLookupUseCase;
    private final MemberRetrievalUseCase memberRetrievalUseCase;
    private final NotificationSenderUseCase notificationService;
    private final ValidationService validationService;
    private final RegisterCommentPort registerCommentPort;
    private final LoadCommentPort loadCommentPort;

    @Transactional
    @Override
    public Long register(Long parentId, Long boardId, CommentRequestDto requestDto) {
        Comment comment = createAndStoreComment(parentId, boardId, requestDto);
        sendNotificationForNewComment(comment);
        return boardId;
    }

    private Comment createAndStoreComment(Long parentId, Long boardId, CommentRequestDto requestDto) {
        String currentMemberId = memberRetrievalUseCase.getCurrentMemberId();
        Board board = boardLookupUseCase.getBoardByIdOrThrow(boardId);
        Comment parent = findParentComment(parentId);
        Comment comment = CommentRequestDto.toEntity(requestDto, board, currentMemberId, parent);
        if (parent != null) {
            parent.addChildComment(comment);
        }
        validationService.checkValid(comment);
        return registerCommentPort.save(comment);
    }

    private Comment findParentComment(Long parentId) {
        return parentId != null ? loadCommentPort.findById(parentId).orElse(null) : null;
    }

    private void sendNotificationForNewComment(Comment comment) {
        Board board = comment.getBoard();
        String notificationMessage = String.format("[%s] 새로운 댓글이 등록되었습니다.", board.getTitle());
        notificationService.sendNotificationToMember(board.getMemberId(), notificationMessage);
    }
}
