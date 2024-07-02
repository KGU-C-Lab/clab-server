package page.clab.api.domain.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.domain.member.application.MemberBirthdayRetrievalThisMonthUseCase;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.response.MemberBirthdayResponseDto;
import page.clab.api.global.common.dto.ApiResponse;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.InvalidColumnException;
import page.clab.api.global.exception.SortingArgumentException;
import page.clab.api.global.util.PageableUtils;

import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "멤버")
public class MemberBirthdayRetrievalThisMonthController {

    private final MemberBirthdayRetrievalThisMonthUseCase memberBirthdayRetrievalThisMonthUseCase;

    @Operation(summary = "이달의 생일자 조회", description = "ROLE_USER 이상의 권한이 필요함<br>" +
            "페이지네이션 정렬에 사용할 수 있는 칼럼 : createdAt, id, updatedAt, birth, grade, lastLoginTime, loanSuspensionDate")
    @GetMapping("/birthday")
    public ApiResponse<PagedResponseDto<MemberBirthdayResponseDto>> retrieveBirthdaysThisMonth(
            @RequestParam(name = "month") int month,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "birth") List<String> sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "asc") List<String> sortDirection
    ) throws SortingArgumentException, InvalidColumnException {
        Pageable pageable = PageableUtils.createPageable(page, size, sortBy, sortDirection, Member.class);
        PagedResponseDto<MemberBirthdayResponseDto> birthdayMembers = memberBirthdayRetrievalThisMonthUseCase.retrieve(month, pageable);
        return ApiResponse.success(birthdayMembers);
    }
}