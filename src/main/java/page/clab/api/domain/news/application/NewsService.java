package page.clab.api.domain.news.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.news.dao.NewsRepository;
import page.clab.api.domain.news.domain.News;
import page.clab.api.domain.news.dto.request.NewsRequestDto;
import page.clab.api.domain.news.dto.request.NewsUpdateRequestDto;
import page.clab.api.domain.news.dto.response.NewsDetailsResponseDto;
import page.clab.api.domain.news.dto.response.NewsResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.file.application.UploadedFileService;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final UploadedFileService uploadedFileService;

    private final ValidationService validationService;

    private final NewsRepository newsRepository;

    @Transactional
    public Long createNews(NewsRequestDto requestDto) {
        News news = NewsRequestDto.toEntity(requestDto);
        validationService.checkValid(news);
        news.setUploadedFiles(uploadedFileService.getUploadedFilesByUrls(requestDto.getFileUrlList()));
        return newsRepository.save(news).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<NewsResponseDto> getNewsByConditions(String category, String title, Pageable pageable) {
        Page<News> newsPage = newsRepository.findByConditions(title, category, pageable);
        return new PagedResponseDto<>(newsPage.map(NewsResponseDto::toDto));
    }

    @Transactional(readOnly = true)
    public NewsDetailsResponseDto getNewsDetails(Long newsId) {
        News news = getNewsByIdOrThrow(newsId);
        return NewsDetailsResponseDto.toDto(news);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<NewsDetailsResponseDto> getDeletedNews(Pageable pageable) {
        Page<News> newsPage = newsRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(newsPage.map(NewsDetailsResponseDto::toDto));
    }

    @Transactional
    public Long updateNews(Long newsId, NewsUpdateRequestDto requestDto) {
        News news = getNewsByIdOrThrow(newsId);
        news.update(requestDto);
        validationService.checkValid(news);
        return newsRepository.save(news).getId();
    }

    public Long deleteNews(Long newsId) {
        News news = getNewsByIdOrThrow(newsId);
        newsRepository.delete(news);
        return news.getId();
    }

    public News getNewsByIdOrThrow(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new NotFoundException("해당 뉴스가 존재하지 않습니다."));
    }

}