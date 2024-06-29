package page.clab.api.domain.news.application;

import org.springframework.data.domain.Pageable;
import page.clab.api.domain.news.dto.response.NewsResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

public interface FetchNewsByConditionsService {
    PagedResponseDto<NewsResponseDto> execute(String title, String category, Pageable pageable);
}