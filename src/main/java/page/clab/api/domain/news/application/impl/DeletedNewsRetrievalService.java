package page.clab.api.domain.news.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.news.application.DeletedNewsRetrievalUseCase;
import page.clab.api.domain.news.dao.NewsRepository;
import page.clab.api.domain.news.domain.News;
import page.clab.api.domain.news.dto.response.NewsDetailsResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedNewsRetrievalService implements DeletedNewsRetrievalUseCase {

    private final NewsRepository newsRepository;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<NewsDetailsResponseDto> retrieve(Pageable pageable) {
        Page<News> newsPage = newsRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(newsPage.map(NewsDetailsResponseDto::toDto));
    }
}
