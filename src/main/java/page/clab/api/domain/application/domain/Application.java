package page.clab.api.domain.application.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;
import page.clab.api.domain.application.dto.request.ApplicationRequestDto;
import page.clab.api.global.util.ModelMapperUtil;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Application {

    @Id
    @Size(min = 9, max = 9, message = "{size.application.studentId}")
    @Pattern(regexp = "^[0-9]+$", message = "{pattern.application.studentId}")
    private String studentId;

    @Column(nullable = false)
    @Size(min = 1, max = 10, message = "{size.application.name}")
    private String name;

    @Column(nullable = false)
    @Size(min = 11, max = 11, message = "{size.application.contact}")
    @Pattern(regexp = "^[0-9]+$", message = "{pattern.application.contact}")
    private String contact;

    @Column(nullable = false)
    @Email(message = "{email.application.email}")
    @Size(min = 1, message = "{size.application.email}")
    private String email;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.application.department}")
    private String department;

    @Column(nullable = false)
    @Min(value = 1, message = "{min.application.grade}")
    @Max(value = 4, message = "{max.application.grade}")
    private Long grade;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    @Size(min = 1, message = "{size.application.address}")
    private String address;

    @Column(nullable = false)
    private String interests;

    @Column(length = 1000)
    @Size(max = 1000, message = "{size.application.otherActivities}")
    private String otherActivities;

    @URL(message = "{url.application.githubUrl}")
    private String githubUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicationType applicationType;

    @NotNull
    private Boolean isPass;

    @CreationTimestamp
    private LocalDateTime updateTime;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static Application of(ApplicationRequestDto applicationRequestDto) {
        return ModelMapperUtil.getModelMapper().map(applicationRequestDto, Application.class);
    }

}