package page.clab.api.domain.review.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.activityGroup.domain.ActivityGroup;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.review.domain.Review;

import java.util.Optional;

public interface RetrieveReviewPort {
    Optional<Review> findById(Long reviewId);

    Review findByIdOrThrow(Long reviewId);

    Page<Review> findAllByIsDeletedTrue(Pageable pageable);

    Page<Review> findAllByMember(Member member, Pageable pageable);

    Page<Review> findByConditions(String memberId, String memberName, Long activityId, Boolean isPublic, Pageable pageable);

    boolean existsByMemberAndActivityGroup(Member member, ActivityGroup activityGroup);
}
