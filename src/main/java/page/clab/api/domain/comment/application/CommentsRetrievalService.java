package page.clab.api.domain.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.comment.application.port.in.CommentsRetrievalUseCase;
import page.clab.api.domain.comment.application.port.out.RetrieveCommentsByBoardIdAndParentIsNullPort;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.comment.dto.response.CommentResponseDto;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsRetrievalService implements CommentsRetrievalUseCase {

    private final RetrieveCommentsByBoardIdAndParentIsNullPort retrieveCommentsByBoardIdAndParentIsNullPort;
    private final MemberLookupUseCase memberLookupUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<CommentResponseDto> retrieve(Long boardId, Pageable pageable) {
        return getAllComments(boardId, pageable);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<CommentResponseDto> getAllComments(Long boardId, Pageable pageable) {
        String currentMemberId = memberLookupUseCase.getCurrentMemberId();
        Page<Comment> comments = retrieveCommentsByBoardIdAndParentIsNullPort.findAllByBoardIdAndParentIsNull(boardId, pageable);
        List<CommentResponseDto> commentDtos = comments.stream()
                .map(comment -> toCommentResponseDtoWithMemberInfo(comment, currentMemberId))
                .toList();
        return new PagedResponseDto<>(new PageImpl<>(commentDtos, pageable, comments.getTotalElements()));
    }

    private CommentResponseDto toCommentResponseDtoWithMemberInfo(Comment comment, String currentMemberId) {
        MemberDetailedInfoDto memberInfo = memberLookupUseCase.getMemberDetailedInfoById(comment.getWriterId());
        List<CommentResponseDto> childrenDtos = comment.getChildren().stream()
                .map(child -> toCommentResponseDtoWithMemberInfo(child, currentMemberId))
                .toList();
        boolean isOwner = comment.isOwner(currentMemberId);
        return CommentResponseDto.toDto(comment, memberInfo, isOwner, childrenDtos);
    }
}