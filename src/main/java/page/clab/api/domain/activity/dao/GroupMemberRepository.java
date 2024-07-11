package page.clab.api.domain.activity.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activity.domain.ActivityGroup;
import page.clab.api.domain.activity.domain.ActivityGroupRole;
import page.clab.api.domain.activity.domain.GroupMember;
import page.clab.api.domain.activity.domain.GroupMemberId;
import page.clab.api.domain.activity.domain.GroupMemberStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId>, GroupMemberRepositoryCustom, QuerydslPredicateExecutor<GroupMember> {
    List<GroupMember> findAllByMemberId(String memberId);

    boolean existsByMemberIdAndActivityGroupId(String memberId, Long activityGroupId);

    Optional<GroupMember> findByActivityGroupIdAndRole(Long activityGroupId, ActivityGroupRole role);

    Optional<GroupMember> findByActivityGroupAndMemberId(ActivityGroup activityGroup, String memberId);

    List<GroupMember> findAllByActivityGroupIdOrderByMemberIdAsc(Long activityGroupId);

    Page<GroupMember> findAllByActivityGroupId(Long activityGroupId, Pageable pageable);

    Page<GroupMember> findAllByActivityGroupIdAndStatus(Long activityGroupId, GroupMemberStatus status, org.springframework.data.domain.Pageable pageable);

    boolean existsByActivityGroupAndMemberId(ActivityGroup activityGroup, String memberId);
}