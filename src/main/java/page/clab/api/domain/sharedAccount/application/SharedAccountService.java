package page.clab.api.domain.sharedAccount.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.sharedAccount.dao.SharedAccountRepository;
import page.clab.api.domain.sharedAccount.domain.SharedAccount;
import page.clab.api.domain.sharedAccount.dto.request.SharedAccountRequestDto;
import page.clab.api.domain.sharedAccount.dto.request.SharedAccountUpdateRequestDto;
import page.clab.api.domain.sharedAccount.dto.response.SharedAccountResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class SharedAccountService {

    private final SharedAccountRepository sharedAccountRepository;

    public Long createSharedAccount(SharedAccountRequestDto sharedAccountRequestDto) {
        SharedAccount sharedAccount = SharedAccount.create(sharedAccountRequestDto);
        return sharedAccountRepository.save(sharedAccount).getId();
    }

    public PagedResponseDto<SharedAccountResponseDto> getSharedAccounts(Pageable pageable) {
        Page<SharedAccount> sharedAccounts = sharedAccountRepository.findAllByOrderByIdAsc(pageable);
        return new PagedResponseDto<>(sharedAccounts.map(SharedAccountResponseDto::of));
    }

    public Long updateSharedAccount(Long accountId, SharedAccountUpdateRequestDto sharedAccountUpdateRequestDto) {
        SharedAccount sharedAccount = getSharedAccountByIdOrThrow(accountId);
        sharedAccount.update(sharedAccountUpdateRequestDto);
        return sharedAccountRepository.save(sharedAccount).getId();
    }

    public Long deleteSharedAccount(Long accountId) {
        SharedAccount sharedAccount = getSharedAccountByIdOrThrow(accountId);
        sharedAccountRepository.delete(sharedAccount);
        return sharedAccount.getId();
    }

    public SharedAccount getSharedAccountByIdOrThrow(Long accountId) {
        return sharedAccountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 계정입니다."));
    }

    public SharedAccount save(SharedAccount sharedAccount) {
        return sharedAccountRepository.save(sharedAccount);
    }

}
