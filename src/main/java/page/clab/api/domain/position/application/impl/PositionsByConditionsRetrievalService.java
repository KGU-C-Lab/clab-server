package page.clab.api.domain.position.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberPositionInfoDto;
import page.clab.api.domain.position.application.PositionsByConditionsRetrievalUseCase;
import page.clab.api.domain.position.dao.PositionRepository;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;
import page.clab.api.domain.position.dto.response.PositionResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class PositionsByConditionsRetrievalService implements PositionsByConditionsRetrievalUseCase {

    private final PositionRepository positionRepository;
    private final MemberLookupUseCase memberLookupUseCase;

    @Transactional(readOnly = true)
    public PagedResponseDto<PositionResponseDto> retrieve(String year, PositionType positionType, Pageable pageable) {
        MemberPositionInfoDto currentMemberInfo = memberLookupUseCase.getCurrentMemberPositionInfo();
        Page<Position> positions = positionRepository.findByConditions(year, positionType, pageable);
        return new PagedResponseDto<>(positions.map(position -> PositionResponseDto.toDto(position, currentMemberInfo)));
    }
}