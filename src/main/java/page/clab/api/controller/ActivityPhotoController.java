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
import page.clab.api.service.ActivityPhotoService;
import page.clab.api.type.dto.ActivityPhotoRequestDto;
import page.clab.api.type.dto.ActivityPhotoResponseDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.dto.ResponseModel;

@RestController
@RequestMapping("/activity-photos")
@RequiredArgsConstructor
@Tag(name = "ActivityPhoto", description = "활동 사진 관련 API")
@Slf4j
public class ActivityPhotoController {

    private final ActivityPhotoService activityPhotoService;

    @Operation(summary = "[A] 활동 사진 등록", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PostMapping("")
    public ResponseModel createActivityPhoto(
            @Valid @RequestBody ActivityPhotoRequestDto activityPhotoRequestDto,
            BindingResult result
    ) throws MethodArgumentNotValidException, PermissionDeniedException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }
        activityPhotoService.createActivityPhoto(activityPhotoRequestDto);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "활동 사진 목록 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @GetMapping("")
    public ResponseModel getActivityPhotos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ActivityPhotoResponseDto> activityPhotos = activityPhotoService.getActivityPhotos(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityPhotos);
        return responseModel;
    }

    @Operation(summary = "공개된 활동 사진 목록 조회", description = "ROLE_ANONYMOUS 이상의 권한이 필요함")
    @GetMapping("/public")
    public ResponseModel getPublicActivityPhotos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PagedResponseDto<ActivityPhotoResponseDto> activityPhotos = activityPhotoService.getPublicActivityPhotos(pageable);
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData(activityPhotos);
        return responseModel;
    }

    @Operation(summary = "활동 사진 고정/해제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @PatchMapping("/{activityPhotoId}")
    public ResponseModel updateActivityPhoto(
            @PathVariable Long activityPhotoId
    ) throws PermissionDeniedException {
        activityPhotoService.updateActivityPhoto(activityPhotoId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

    @Operation(summary = "[A] 활동 사진 삭제", description = "ROLE_ADMIN 이상의 권한이 필요함")
    @DeleteMapping("/{activityPhotoId}")
    public ResponseModel deleteActivityPhoto(
            @PathVariable Long activityPhotoId
    ) throws PermissionDeniedException {
        activityPhotoService.deleteActivityPhoto(activityPhotoId);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;
    }

}