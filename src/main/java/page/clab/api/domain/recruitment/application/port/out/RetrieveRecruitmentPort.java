package page.clab.api.domain.recruitment.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.recruitment.domain.Recruitment;

import java.util.List;
import java.util.Optional;

public interface RetrieveRecruitmentPort {
    Optional<Recruitment> findById(Long recruitmentId);

    Recruitment findByIdOrThrow(Long recruitmentId);

    List<Recruitment> findAll();

    Page<Recruitment> findAllByIsDeletedTrue(Pageable pageable);

    List<Recruitment> findTop5ByOrderByCreatedAtDesc();
}
