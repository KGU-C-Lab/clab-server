package page.clab.api.domain.review.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.review.application.FetchDeletedReviewsService;
import page.clab.api.domain.review.dao.ReviewRepository;
import page.clab.api.domain.review.domain.Review;
import page.clab.api.domain.review.dto.response.ReviewResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class FetchDeletedReviewsServiceImpl implements FetchDeletedReviewsService {

    private final MemberLookupService memberLookupService;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<ReviewResponseDto> execute(Pageable pageable) {
        Member currentMember = memberLookupService.getCurrentMember();
        Page<Review> reviews = reviewRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(reviews.map(review -> ReviewResponseDto.toDto(review, currentMember)));
    }
}