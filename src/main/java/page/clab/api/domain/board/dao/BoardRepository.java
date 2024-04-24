package page.clab.api.domain.board.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.BoardCategory;
import page.clab.api.domain.member.domain.Member;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

    Page<Board> findAllByIsDeletedFalseAndMemberOrderByCreatedAtDesc(Member member, Pageable pageable);

    Page<Board> findAllByIsDeletedFalseAndCategoryOrderByCreatedAtDesc(BoardCategory category, Pageable pageable);

    Page<Board> findAllByIsDeletedTrue(Pageable pageable);

    Optional<Board> findByIsDeletedFalseAndId(Long id);

}
