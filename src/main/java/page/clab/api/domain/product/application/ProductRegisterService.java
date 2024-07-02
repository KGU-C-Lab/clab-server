package page.clab.api.domain.product.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.product.application.port.in.ProductRegisterUseCase;
import page.clab.api.domain.product.application.port.out.RegisterProductPort;
import page.clab.api.domain.product.domain.Product;
import page.clab.api.domain.product.dto.request.ProductRequestDto;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class ProductRegisterService implements ProductRegisterUseCase {

    private final ValidationService validationService;
    private final RegisterProductPort registerProductPort;

    @Transactional
    @Override
    public Long register(ProductRequestDto requestDto) {
        Product product = ProductRequestDto.toEntity(requestDto);
        validationService.checkValid(product);
        return registerProductPort.save(product).getId();
    }
}