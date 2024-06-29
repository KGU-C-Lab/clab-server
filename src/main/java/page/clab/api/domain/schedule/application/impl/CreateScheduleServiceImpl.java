package page.clab.api.domain.schedule.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.activityGroup.application.ActivityGroupAdminService;
import page.clab.api.domain.activityGroup.application.ActivityGroupMemberService;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.activityGroup.domain.ActivityGroupRole;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.schedule.application.CreateScheduleService;
import page.clab.api.domain.schedule.dao.ScheduleRepository;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.domain.schedule.domain.ScheduleType;
import page.clab.api.domain.schedule.dto.request.ScheduleRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateScheduleServiceImpl implements CreateScheduleService {

    private final MemberLookupService memberLookupService;
    private final ActivityGroupMemberService activityGroupMemberService;
    private final ActivityGroupAdminService activityGroupAdminService;
    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public Long execute(ScheduleRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = memberLookupService.getCurrentMember();
        ActivityGroup activityGroup = resolveActivityGroupForSchedule(requestDto, currentMember);
        Schedule schedule = ScheduleRequestDto.toEntity(requestDto, currentMember, activityGroup);
        schedule.validateAccessPermissionForCreation(currentMember);
        return scheduleRepository.save(schedule).getId();
    }

    private ActivityGroup resolveActivityGroupForSchedule(ScheduleRequestDto requestDto, Member member) throws PermissionDeniedException {
        ScheduleType scheduleType = requestDto.getScheduleType();
        ActivityGroup activityGroup = null;
        if (!scheduleType.equals(ScheduleType.ALL)) {
            Long activityGroupId = Optional.ofNullable(requestDto.getActivityGroupId())
                    .orElseThrow(() -> new NullPointerException("스터디 또는 프로젝트 일정은 그룹 id를 입력해야 합니다."));
            activityGroup = activityGroupAdminService.getActivityGroupByIdOrThrow(activityGroupId);
            validateMemberIsGroupLeaderOrAdmin(member, activityGroup);
        }
        return activityGroup;
    }

    private void validateMemberIsGroupLeaderOrAdmin(Member member, ActivityGroup activityGroup) throws PermissionDeniedException {
        GroupMember groupMember = activityGroupMemberService.getGroupMemberByActivityGroupIdAndRole(activityGroup.getId(), ActivityGroupRole.LEADER);
        if (groupMember != null && !member.isAdminRole() && !member.isSameMember(groupMember.getMember())) {
            throw new PermissionDeniedException("해당 스터디 또는 프로젝트의 LEADER, 관리자만 그룹 일정을 추가할 수 있습니다.");
        }
    }
}