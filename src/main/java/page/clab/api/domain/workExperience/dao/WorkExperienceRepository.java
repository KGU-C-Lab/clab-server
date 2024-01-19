package page.clab.api.domain.workExperience.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import page.clab.api.domain.workExperience.domain.WorkExperience;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {

    Page<WorkExperience> findAllByMember_IdOrderByStartDateDesc(String memberId, Pageable pageable);

}