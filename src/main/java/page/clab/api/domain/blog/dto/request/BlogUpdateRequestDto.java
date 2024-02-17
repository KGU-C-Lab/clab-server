package page.clab.api.domain.blog.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogUpdateRequestDto {

    @Size(min = 1, max = 255, message = "{size.blog.title}")
    @Schema(description = "제목", example = "Swagger Docs의 접근 권한을 제어하기 위한 여정")
    private String title;

    @Size(min = 1, max = 255, message = "{size.blog.subTitle}")
    @Schema(description = "부제목", example = "Basic Auth와 JWT를 같이 사용하기 위한 Spring Security 디버깅")
    private String subTitle;

    @Size(min = 1, max = 10000, message = "{size.blog.content}")
    @Schema(description = "내용", example = "NestJs는 스웨거 설정에 있던데 스프링은........")
    private String content;

    @Schema(description = "이미지 URL", example = "https://www.clab.page/assets/logoWhite-fc1ef9a0.webp")
    private String imageUrl;

}