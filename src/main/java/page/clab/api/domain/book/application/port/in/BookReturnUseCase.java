package page.clab.api.domain.book.application.port.in;

import page.clab.api.domain.book.dto.request.BookLoanRecordRequestDto;

public interface BookReturnUseCase {
    Long returnBook(BookLoanRecordRequestDto requestDto);
}