package page.clab.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.CommentRepository;
import page.clab.api.type.dto.CommentRequestDto;
import page.clab.api.type.dto.CommentResponseDto;
import page.clab.api.type.entity.Board;
import page.clab.api.type.entity.Comment;
import page.clab.api.type.entity.Member;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final BoardService boardService;

    private final MemberService memberService;

    public void createComment(Long boardId, CommentRequestDto commentRequestDto) {
        Member member = memberService.getCurrentMember();
        Board board = boardService.getBoardByIdOrThrow(boardId);
        Comment comment = Comment.of(commentRequestDto);
        comment.setBoard(board);
        comment.setWriter(member);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    public List<CommentResponseDto> getComments(Long boardId) {
        List<Comment> comments = commentRepository.findAllByBoardId(boardId);
        return comments.stream()
                .map(CommentResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<CommentResponseDto> getMyComments() {
        Member member = memberService.getCurrentMember();
        List<Comment> comments = getCommentsByWriter(member);
        return comments.stream()
                .map(CommentResponseDto::of)
                .collect(Collectors.toList());
    }

    public void updateComment(Long commentId, CommentRequestDto commentRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Comment comment = getCommentByIdOrThrow(commentId);
        if (!(comment.getWriter().getId().equals(member.getId()) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("댓글 작성자만 수정할 수 있습니다.");
        }
        comment.setContent(commentRequestDto.getContent());
        comment.setUpdateTime(LocalDateTime.now());
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) throws PermissionDeniedException{
        Member member = memberService.getCurrentMember();
        Comment comment = getCommentByIdOrThrow(commentId);
        if (!(comment.getWriter().getId().equals(member.getId()) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("댓글 작성자만 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
    }

    public Comment getCommentByIdOrThrow(Long id){
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
    }

    private List<Comment> getCommentsByWriter(Member member) {
        return commentRepository.findAllByWriter(member);
    }

}