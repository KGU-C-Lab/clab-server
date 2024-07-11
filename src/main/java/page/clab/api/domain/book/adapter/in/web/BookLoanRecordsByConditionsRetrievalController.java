package page.clab.api.domain.book.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.book.adapter.out.persistence.BookLoanRecordJpaEntity;
import page.clab.api.domain.book.application.dto.response.BookLoanRecordResponseDto;
import page.clab.api.domain.book.application.port.in.RetrieveBookLoanRecordsByConditionsUseCase;
import page.clab.api.domain.book.domain.BookLoanStatus;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book-loan-records")
@RequiredArgsConstructor
@Tag(name = "BookLoanRecord", description = "도서 대출")
public class BookLoanRecordsByConditionsRetrievalController {

    private final RetrieveBookLoanRecordsByConditionsUseCase retrieveBookLoanRecordsByConditionsUseCase;

    @Operation(summary = "[U] 도서 대출 내역 조회(도서 ID, 대출자 ID, 대출 상태 기준)", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "3개의 파라미터를 자유롭게 조합하여 필터링 가능<br>" +
            "도서 ID, 대출자 ID, 대출 가능 여부 중 하나라도 입력하지 않으면 전체 조회됨<br>" +
            "페이지네이션 정렬에 사용할 수 있는 칼럼 : createdAt, id, updatedAt, bookId, borrowedAt, dueDate, loanExtensionCount, returnedAt, memberId")
    @Secured({ "ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER" })
    @GetMapping("/conditions")
    public ApiResponse<PagedResponseDto<BookLoanRecordResponseDto>> retrieveBookLoanRecordsByConditions(
            @RequestParam(name = "bookId", required = false) Long bookId,
            @RequestParam(name = "borrowerId", required = false) String borrowerId,
            @RequestParam(name = "status", required = false) BookLoanStatus status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "borrowedAt, createdAt") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc, asc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = PageableUtils.createPageable(page, size, sortBy, sortDirection, BookLoanRecordJpaEntity.class);
        PagedResponseDto<BookLoanRecordResponseDto> bookLoanRecords =
                retrieveBookLoanRecordsByConditionsUseCase.retrieveBookLoanRecords(bookId, borrowerId, status, pageable);
        return ApiResponse.success(bookLoanRecords);
    }
}