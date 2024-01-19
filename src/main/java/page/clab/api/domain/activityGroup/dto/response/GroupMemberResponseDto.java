package page.clab.api.domain.activityGroup.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activityGroup.domain.GroupMember;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupMemberResponseDto {

    String memberId;

    String memberName;

    String role;

    public static GroupMemberResponseDto of(GroupMember groupMember) {
        GroupMemberResponseDto groupMemberResponseDto = ModelMapperUtil.getModelMapper().map(groupMember, GroupMemberResponseDto.class);
        groupMemberResponseDto.setMemberId(groupMember.getMember().getId());
        groupMemberResponseDto.setMemberName(groupMember.getMember().getName());
        return groupMemberResponseDto;
    }

    public static List<GroupMemberResponseDto> of(List<GroupMember> groupMembers) {
        return groupMembers.stream()
                .map(GroupMemberResponseDto::of)
                .toList();
    }

}