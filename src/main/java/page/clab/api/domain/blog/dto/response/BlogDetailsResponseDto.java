package page.clab.api.domain.blog.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.blog.domain.Blog;
import page.clab.api.global.util.ModelMapperUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogDetailsResponseDto {

    private Long id;

    private String memberId;

    private String name;

    private String title;

    private String subTitle;

    private String content;

    private String imageUrl;

    private LocalDateTime createdAt;

    public static BlogDetailsResponseDto of(Blog blog) {
        BlogDetailsResponseDto blogResponseDto = ModelMapperUtil.getModelMapper().map(blog, BlogDetailsResponseDto.class);
        blogResponseDto.setMemberId(blog.getMember().getId());
        blogResponseDto.setName(blog.getMember().getName());
        return blogResponseDto;
    }

}