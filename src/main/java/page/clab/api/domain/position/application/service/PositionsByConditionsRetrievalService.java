package page.clab.api.domain.position.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.dto.shared.MemberPositionInfoDto;
import page.clab.api.domain.position.application.port.in.RetrievePositionsByConditionsUseCase;
import page.clab.api.domain.position.application.port.out.RetrievePositionPort;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;
import page.clab.api.domain.position.dto.response.PositionResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class PositionsByConditionsRetrievalService implements RetrievePositionsByConditionsUseCase {

    private final RetrievePositionPort retrievePositionPort;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;

    @Transactional(readOnly = true)
    public PagedResponseDto<PositionResponseDto> retrievePositions(String year, PositionType positionType, Pageable pageable) {
        MemberPositionInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberPositionInfo();
        Page<Position> positions = retrievePositionPort.findByConditions(year, positionType, pageable);
        return new PagedResponseDto<>(positions.map(position -> PositionResponseDto.toDto(position, currentMemberInfo)));
    }
}