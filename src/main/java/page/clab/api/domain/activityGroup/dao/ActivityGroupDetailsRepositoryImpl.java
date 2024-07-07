package page.clab.api.domain.activityGroup.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoard;
import page.clab.api.domain.activityGroup.domain.ActivityGroupBoardCategory;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.domain.activityGroup.domain.QActivityGroup;
import page.clab.api.domain.activityGroup.domain.QActivityGroupBoard;
import page.clab.api.domain.activityGroup.domain.QGroupMember;
import page.clab.api.domain.activityGroup.dto.param.ActivityGroupDetails;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ActivityGroupDetailsRepositoryImpl implements ActivityGroupDetailsRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public ActivityGroupDetails fetchActivityGroupDetails(Long activityGroupId) {
        QActivityGroup qActivityGroup = QActivityGroup.activityGroup;
        QGroupMember qGroupMember = QGroupMember.groupMember;
        QActivityGroupBoard qActivityGroupBoard = QActivityGroupBoard.activityGroupBoard;

        BooleanBuilder groupMemberCondition = new BooleanBuilder();
        if (activityGroupId != null) groupMemberCondition.and(qGroupMember.activityGroup.id.eq(activityGroupId));

        List<GroupMember> groupMembers = queryFactory.selectFrom(qGroupMember)
                .where(groupMemberCondition)
                .fetch();

        BooleanBuilder boardCondition = new BooleanBuilder();
        if (activityGroupId != null) boardCondition.and(qActivityGroupBoard.activityGroup.id.eq(activityGroupId));
        if (activityGroupId != null) boardCondition.and(qActivityGroupBoard.category.in(
                ActivityGroupBoardCategory.NOTICE,
                ActivityGroupBoardCategory.WEEKLY_ACTIVITY,
                ActivityGroupBoardCategory.ASSIGNMENT)
        );

        List<ActivityGroupBoard> boards = queryFactory.selectFrom(qActivityGroupBoard)
                .where(boardCondition)
                .orderBy(qActivityGroupBoard.createdAt.desc())
                .fetch();

        BooleanBuilder activityGroupCondition = new BooleanBuilder();
        if (activityGroupId != null) activityGroupCondition.and(qActivityGroup.id.eq(activityGroupId));

        ActivityGroup foundActivityGroup = queryFactory.selectFrom(qActivityGroup)
                .where(activityGroupCondition)
                .fetchOne();

        return ActivityGroupDetails.create(foundActivityGroup, groupMembers, boards);
    }

}
