package page.clab.api.domain.schedule.application;

import page.clab.api.global.exception.PermissionDeniedException;

public interface ScheduleRemoveUseCase {
    Long remove(Long scheduleId) throws PermissionDeniedException;
}