package page.clab.api.domain.schedule.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.schedule.application.SchedulesByConditionsRetrievalUseCase;
import page.clab.api.domain.schedule.dao.ScheduleRepository;
import page.clab.api.domain.schedule.domain.Schedule;
import page.clab.api.domain.schedule.domain.SchedulePriority;
import page.clab.api.domain.schedule.dto.response.ScheduleResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class SchedulesByConditionsRetrievalService implements SchedulesByConditionsRetrievalUseCase {

    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<ScheduleResponseDto> retrieve(Integer year, Integer month, SchedulePriority priority, Pageable pageable) {
        Page<Schedule> schedules = scheduleRepository.findByConditions(year, month, priority, pageable);
        return new PagedResponseDto<>(schedules.map(ScheduleResponseDto::toDto));
    }
}