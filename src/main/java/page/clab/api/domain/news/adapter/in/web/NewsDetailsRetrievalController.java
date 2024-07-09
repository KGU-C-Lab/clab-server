package page.clab.api.domain.news.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.news.application.port.in.RetrieveNewsDetailsUseCase;
import page.clab.api.domain.news.application.dto.response.NewsDetailsResponseDto;
import page.clab.api.global.common.dto.ApiResponse;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@Tag(name = "News", description = "뉴스")
public class NewsDetailsRetrievalController {

    private final RetrieveNewsDetailsUseCase retrieveNewsDetailsUseCase;

    @Operation(summary = "[U] 뉴스 상세 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER" })
    @GetMapping("/{newsId}")
    public ApiResponse<NewsDetailsResponseDto> retrieveNewsDetails(
            @PathVariable(name = "newsId") Long newsId
    ) {
        NewsDetailsResponseDto news = retrieveNewsDetailsUseCase.retrieveNewsDetails(newsId);
        return ApiResponse.success(news);
    }
}

