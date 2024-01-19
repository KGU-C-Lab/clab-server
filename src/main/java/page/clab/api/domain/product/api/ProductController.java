package page.clab.api.domain.product.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
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
import page.clab.api.domain.product.application.ProductService;
import page.clab.api.domain.product.dto.request.ProductRequestDto;
import page.clab.api.domain.product.dto.response.ProductResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.dto.ResponseModel;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "서비스 관련 API")
@Slf4j
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "[A] 서비스 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PostMapping("")
    public ResponseModel createProduct(
            @Valid @RequestBody ProductRequestDto productRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = productService.createProduct(productRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[U] 서비스 조회", description = "ROLE_USER 이상의 권한이 필요함")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("")
    public ResponseModel getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ProductResponseDto> productResponseDtos = productService.getProducts(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(productResponseDtos);
        return responseModel;
    }

    @Operation(summary = "[U] 서비스 검색", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "서비스명을 기준으로 검색")
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER"})
    @GetMapping("/search")
    public ResponseModel searchProduct(
            @RequestParam String productName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ProductResponseDto> productResponseDtos = productService.searchProduct(productName, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(productResponseDtos);
        return responseModel;
    }

    @Operation(summary = "[A] 서비스 수정", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @PatchMapping("/{productId}")
    public ResponseModel updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequestDto productRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        Long id = productService.updateProduct(productId, productRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

    @Operation(summary = "[A] 서비스 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @Secured({"ROLE_ADMIN", "ROLE_SUPER"})
    @DeleteMapping("")
    public ResponseModel deleteProduct(
            @RequestParam Long productId
    ) {
        Long id = productService.deleteProduct(productId);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(id);
        return responseModel;
    }

}
