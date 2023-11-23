package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.BlogService;
import page.clab.api.type.dto.BlogDetailsResponseDto;
import page.clab.api.type.dto.BlogRequestDto;
import page.clab.api.type.dto.BlogResponseDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/blogs")
@RequiredArgsConstructor
@Tag(name = "Blog", description = "블로그 포스트 관련 API")
@Slf4j
public class BlogController {

    private final BlogService blogService;

    @Operation(summary = "[U] 블로그 포스트 생성", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("")
    public ResponseModel createBlog(
            @Valid @RequestBody BlogRequestDto blogRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        blogService.createBlog(blogRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 블로그 포스트 목록 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("")
    public ResponseModel getBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<BlogResponseDto> blogs = blogService.getBlogs(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(blogs);
        return responseModel;
    }

    @Operation(summary = "[U] 블로그 포스트 상세 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @GetMapping("/{blogId}")
    public ResponseModel getBlogDetails(
            @PathVariable Long blogId
    ) {
        BlogDetailsResponseDto blog = blogService.getBlogDetails(blogId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(blog);
        return responseModel;
    }

    @Operation(summary = "[U] 블로그 포스트 검색", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "검색어에는 제목, 부제목, 내용, 작성자명가 포함됨")
    @GetMapping("/search")
    public ResponseModel searchBlog(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<BlogResponseDto> blogs = blogService.searchBlog(keyword, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(blogs);
        return responseModel;
    }

    @Operation(summary = "[U] 블로그 포스트 수정", description = "ROLE_USER 이상의 권한이 필요함")
    @PatchMapping("/{blogId}")
    public ResponseModel updateBlog(
            @PathVariable Long blogId,
            @Valid @RequestBody BlogRequestDto blogRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        blogService.updateBlog(blogId, blogRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[U] 블로그 포스트 삭제", description = "ROLE_USER 이상의 권한이 필요함")
    @DeleteMapping("/{blogId}")
    public ResponseModel deleteBlog(
            @PathVariable Long blogId
    ) throws PermissionDeniedException {
        blogService.deleteBlog(blogId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}
