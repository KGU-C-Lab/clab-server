package page.clab.api.domain.community.board.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.community.board.domain.BoardCategory;

import java.util.List;
import java.util.Optional;

public interface RetrieveBoardPort {
    Optional<Board> findById(Long boardId);

    Board findByIdOrThrow(Long boardId);

    Page<Board> findAll(Pageable pageable);

    Page<Board> findAllByCategory(BoardCategory category, Pageable pageable);

    Page<Board> findAllByIsDeletedTrue(Pageable pageable);

    Page<Board> findAllByMemberId(String memberId, Pageable pageable);

    List<Board> findByMemberId(String memberId);
}