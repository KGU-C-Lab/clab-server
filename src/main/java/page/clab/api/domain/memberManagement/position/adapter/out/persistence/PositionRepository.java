package page.clab.api.domain.memberManagement.position.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import page.clab.api.domain.memberManagement.position.domain.PositionType;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<PositionJpaEntity, Long>, PositionRepositoryCustom, QuerydslPredicateExecutor<PositionJpaEntity> {

    List<PositionJpaEntity> findByMemberId(String id);

    Optional<PositionJpaEntity> findByMemberIdAndYearAndPositionType(String memberId, String year, PositionType positionType);

    List<PositionJpaEntity> findAllByMemberIdAndYearOrderByPositionTypeAsc(String memberId, String year);
}
