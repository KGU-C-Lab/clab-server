package page.clab.api.type.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;
import page.clab.api.type.dto.NewsRequestDto;
import page.clab.api.util.ModelMapperUtil;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1)
    private String category;

    @Column(nullable = false)
    @Size(min = 1, max = 100)
    private String title;

    @Size(max = 100)
    private String subtitle;

    @Column(nullable = false, length = 10000)
    @Size(min = 1, max = 10000, message = "{size.news.content}")
    private String content;

    @URL
    private String url;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    public static News of(NewsRequestDto newsRequestDto) {
        return ModelMapperUtil.getModelMapper().map(newsRequestDto, News.class);
    }

}