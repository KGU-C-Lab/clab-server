package page.clab.api.domain.library.bookLoanRecord.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import page.clab.api.domain.library.bookLoanRecord.domain.BookLoanStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookLoanRecordResponseDto {

    private Long bookLoanRecordId;
    private Long bookId;
    private String bookTitle;
    private String bookImageUrl;
    private String borrowerId;
    private String borrowerName;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;
    private LocalDateTime dueDate;
    private Long loanExtensionCount;
    private BookLoanStatus status;
}
