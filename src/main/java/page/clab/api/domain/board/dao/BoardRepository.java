package page.clab.api.domain.board.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.BoardCategory;
import page.clab.api.domain.member.domain.Member;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAll(Pageable pageable);

    Page<Board> findAllByMember(Member member, Pageable pageable);

    Page<Board> findAllByCategory(BoardCategory category, Pageable pageable);

    @Query(value = "SELECT b.* FROM board b WHERE b.is_deleted = true", nativeQuery = true)
    Page<Board> findAllByIsDeletedTrue(Pageable pageable);

}
