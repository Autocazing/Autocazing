package e204.autocazing.vendor.controller;

import e204.autocazing.db.entity.VendorEntity;
import e204.autocazing.stock.dto.PostStockDto;
import e204.autocazing.stock.dto.StockDetailsDto;
import e204.autocazing.vendor.dto.PatchVendorDto;
import e204.autocazing.vendor.dto.PostVendorDto;
import e204.autocazing.vendor.dto.VendorDto;
import e204.autocazing.vendor.service.VendorService;
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
public class VendorController {
    @Autowired
    private VendorService vendorService;


    @Operation(summary = "발주업체 등록 요청", description = "발주업체 등록요청을 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "업체 등록 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostVendorDto.class)
                    )
            )
    })
    @PostMapping("")
    public ResponseEntity createVendor(@RequestBody PostVendorDto postVendorDto) {
         vendorService.createVendor(postVendorDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    // 업체 수정
    @Operation(summary = "발주 업체 수정", description = "발주업체 수정을 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업체 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VendorDto.class)
                    )
            )
    })
    @PatchMapping("/{vendorId}")
    public ResponseEntity updateVendor(@PathVariable(name = "vendorId") Integer vendorId, @RequestBody PatchVendorDto patchVendorDto) {
        VendorDto vendorDto = vendorService.updateVendor(vendorId, patchVendorDto);
        return ResponseEntity.ok(vendorDto);
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
        vendorService.deleteVendor(vendorId);
        return ResponseEntity.ok().build();
    }

    // 재고 상세 조회
    @Operation(summary = "발주업체 상세 조회", description = "발주업체 상세조회를 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "발주업체 상세조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VendorDto.class)
                    )
            )
    })
    @GetMapping("/{vendorId}")
    public ResponseEntity<VendorDto> getVendorById(@PathVariable(name = "vendorId") Integer vendorId) {
        VendorDto vendor = vendorService.getVendorById(vendorId);
        return ResponseEntity.ok(vendor);
    }

    // 전체 재고 조회
    @Operation(summary = "발주업체 전체 조회", description = "발주업체 전체조회를 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "발주업체 전체 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = VendorDto.class)))

            )
    })
    @GetMapping("")
    public ResponseEntity<List<VendorDto>> getAllVendors() {
        List<VendorDto> vendors = vendorService.getAllVendors();
        return ResponseEntity.ok(vendors);
    }
}
