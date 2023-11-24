package page.clab.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.service.AccuseService;
import page.clab.api.type.dto.AccuseRequestDto;
import page.clab.api.type.dto.AccuseResponseDto;
import page.clab.api.type.dto.ResponseModel;
import page.clab.api.type.etc.AccuseStatus;
import page.clab.api.type.etc.TargetType;

@RestController
@RequestMapping("/accuses")
@RequiredArgsConstructor
@Tag(name = "Accuse", description = "신고 관련 API")
@Slf4j
public class AccuseController {

    private final AccuseService accuseService;

    @Operation(summary = "[U] 신고하기", description = "ROLE_USER 이상의 권한이 필요함")
    @PostMapping("")
    public ResponseModel createAccuse(
            @Valid @RequestBody AccuseRequestDto accuseRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        accuseService.createAccuse(accuseRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }
    
    @Operation(summary = "[A] 신고 내역 조회", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @GetMapping("")
    public ResponseModel getAccuses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) throws PermissionDeniedException {
        Pageable pageable = PageRequest.of(page, size);
        List<AccuseResponseDto> accuses = accuseService.getAccuses(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(accuses);
        return responseModel;
    }
    
    @Operation(summary = "[A] 유형/상태별 신고 내역 조회", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @GetMapping("/search")
    public ResponseModel searchAccuse(
            @RequestParam(required = false) TargetType targetType,
            @RequestParam(required = false) AccuseStatus accuseStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) throws PermissionDeniedException {
        Pageable pageable = PageRequest.of(page, size);
        List<AccuseResponseDto> accuses = accuseService.searchAccuse(targetType, accuseStatus, pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(accuses);
        return responseModel;
    }
    
    @Operation(summary = "[A] 신고 상태 변경", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PatchMapping("/{accuseId}")
    public ResponseModel updateAccuseStatus(
            @PathVariable Long accuseId,
            @RequestParam AccuseStatus accuseStatus
    ) throws PermissionDeniedException {
        accuseService.updateAccuseStatus(accuseId, accuseStatus);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }
    
}