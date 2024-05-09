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
@RequestMapping("api/venders")
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
    public ResponseEntity createVender(@RequestBody PostVenderDto postVenderDto) {
         venderService.createVender(postVenderDto);
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
    @PatchMapping("/{venderId}")
    public ResponseEntity updateVender(@PathVariable(name = "venderId") Integer venderId, @RequestBody PatchVenderDto patchVenderDto) {
        VenderDto venderDto = venderService.updateVender(venderId, patchVenderDto);
        return ResponseEntity.ok(venderDto);
    }

    // 재고 삭제
    @Operation(summary = "발주 업체 삭제", description = "발주업체 삭제 요청을 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "발주 업체 삭제 성공",
                    content = @Content(examples = {
                            @ExampleObject(
                                    name = "Vender 삭제 ",
                                    summary = "Vender 삭제 body의 예시",
                                    value = " "
                            )
                    })
            )
    })
    @DeleteMapping("/{venderId}")
    public ResponseEntity deleteVender(@PathVariable(name = "venderId") Integer venderId) {
        venderService.deleteVender(venderId);
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
    @GetMapping("/{venderId}")
    public ResponseEntity<VenderDto> getVenderById(@PathVariable(name = "venderId") Integer venderId) {
        VenderDto vender = venderService.getVenderById(venderId);
        return ResponseEntity.ok(vender);
    }

    // 전체 재고 조회
    @Operation(summary = "발주업체 전체 조회", description = "발주업체 전체조회를 수행하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "발주업체 전체 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = VenderDto.class)))

            )
    })
    @GetMapping("")
    public ResponseEntity<List<VenderDto>> getAllVenders() {
        List<VenderDto> venders = venderService.getAllVenders();
        return ResponseEntity.ok(venders);
    }
}
