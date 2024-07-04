package page.clab.api.domain.schedule.application.port.in;

import page.clab.api.domain.schedule.dto.response.ScheduleCollectResponseDto;

public interface RetrieveCollectSchedulesUseCase {
    ScheduleCollectResponseDto retrieveCollectSchedules();
}