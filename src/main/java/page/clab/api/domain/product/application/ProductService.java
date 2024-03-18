package page.clab.api.domain.product.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.product.dao.ProductRepository;
import page.clab.api.domain.product.domain.Product;
import page.clab.api.domain.product.dto.request.ProductRequestDto;
import page.clab.api.domain.product.dto.request.ProductUpdateRequestDto;
import page.clab.api.domain.product.dto.response.ProductResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Long createProduct(ProductRequestDto productRequestDto) {
        Product product = Product.of(productRequestDto);
        return productRepository.save(product).getId();
    }

    public PagedResponseDto<ProductResponseDto> getProductsByConditions(String productName, Pageable pageable) {
        Page<Product> products = productRepository.findByConditions(productName, pageable);
        return new PagedResponseDto<>(products.map(ProductResponseDto::of));
    }

    public Long updateProduct(Long productId, ProductUpdateRequestDto productUpdateRequestDto) {
        Product product = getProductByIdOrThrow(productId);
        product.update(productUpdateRequestDto);
        return productRepository.save(product).getId();
    }

    public Long deleteProduct(Long productId) {
        Product product = getProductByIdOrThrow(productId);
        productRepository.delete(product);
        return product.getId();
    }

    private Product getProductByIdOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 서비스가 존재하지 않습니다."));
    }

}
