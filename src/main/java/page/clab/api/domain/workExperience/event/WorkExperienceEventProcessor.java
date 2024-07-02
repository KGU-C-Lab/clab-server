package page.clab.api.domain.workExperience.event;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.event.MemberEventProcessor;
import page.clab.api.domain.member.event.MemberEventProcessorRegistry;
import page.clab.api.domain.workExperience.dao.WorkExperienceRepository;
import page.clab.api.domain.workExperience.domain.WorkExperience;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WorkExperienceEventProcessor implements MemberEventProcessor {

    private final WorkExperienceRepository workExperienceRepository;

    private final MemberEventProcessorRegistry processorRegistry;

    @PostConstruct
    public void init() {
        processorRegistry.register(this);
    }

    @Override
    @Transactional
    public void processMemberDeleted(String memberId) {
        List<WorkExperience> workExperiences = workExperienceRepository.findByMemberId(memberId);
        workExperiences.forEach(WorkExperience::delete);
        workExperienceRepository.saveAll(workExperiences);
    }

    @Override
    public void processMemberUpdated(String memberId) {
        // do nothing
    }
}