package page.clab.api.domain.news.application;

import page.clab.api.domain.news.dto.request.NewsUpdateRequestDto;

public interface UpdateNewsService {
    Long execute(Long newsId, NewsUpdateRequestDto requestDto);
}
