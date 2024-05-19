package e204.autocazing.vender.controller;

import e204.autocazing.vender.dto.PatchVenderDto;
import e204.autocazing.vender.dto.PostVenderDto;
import e204.autocazing.vender.dto.VenderDto;
import e204.autocazing.vender.service.VenderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/vendors")
public class VenderController {
    @Autowired
    private VenderService venderService;


    @Operation(summary = "발주업체 등록 요청", description = "발주업체 등록요청을 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "업체 등록 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostVenderDto.class)
                    )
            )
    })
    @PostMapping("")
    public ResponseEntity createVendor(@RequestBody PostVenderDto postVenderDto) {
         venderService.createVendor(postVenderDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    // 업체 수정
    @Operation(summary = "발주 업체 수정", description = "발주업체 수정을 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업체 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VenderDto.class)
                    )
            )
    })
    @PatchMapping("/{vendorId}")
    public ResponseEntity updateVendor(@PathVariable(name = "vendorId") Integer vendorId, @RequestBody PatchVenderDto patchVenderDto) {
        VenderDto venderDto = venderService.updateVendor(vendorId, patchVenderDto);
        return ResponseEntity.ok(venderDto);
    }

    // 재고 삭제
    @Operation(summary = "발주 업체 삭제", description = "발주업체 삭제 요청을 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "발주 업체 삭제 성공",
                    content = @Content(examples = {
                            @ExampleObject(
                                    name = "Vendor 삭제 ",
                                    summary = "Vendor 삭제 body의 예시",
                                    value = " "
                            )
                    })
            )
    })
    @DeleteMapping("/{vendorId}")
    public ResponseEntity deleteVendor(@PathVariable(name = "vendorId") Integer vendorId) {
        venderService.deleteVendor(vendorId);
        return ResponseEntity.ok().build();
    }

    // 재고 상세 조회
    @Operation(summary = "발주업체 상세 조회", description = "발주업체 상세조회를 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "발주업체 상세조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VenderDto.class)
                    )
            )
    })
    @GetMapping("/{vendorId}")
    public ResponseEntity<VenderDto> getVendorById(@PathVariable(name = "vendorId") Integer vendorId) {
        VenderDto vendor = venderService.getVendorById(vendorId);
        return ResponseEntity.ok(vendor);
    }

    // 전체 재고 조회
    @Operation(summary = "발주업체 전체 조회", description = "발주업체 전체조회를 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "발주업체 전체 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = VenderDto.class)))

            )
    })
    @GetMapping("")
    public ResponseEntity<List<VenderDto>> getAllVendors() {
        List<VenderDto> vendors = venderService.getAllVendors();
        return ResponseEntity.ok(vendors);
    }
}
