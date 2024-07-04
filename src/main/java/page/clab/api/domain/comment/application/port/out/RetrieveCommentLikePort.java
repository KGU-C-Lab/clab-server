package page.clab.api.domain.comment.application.port.out;

import page.clab.api.domain.comment.domain.CommentLike;

import java.util.Optional;

public interface RetrieveCommentLikePort {
    Optional<CommentLike> findByCommentIdAndMemberId(Long commentId, String memberId);
}