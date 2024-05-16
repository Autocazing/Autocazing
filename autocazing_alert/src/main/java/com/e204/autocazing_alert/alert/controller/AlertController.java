package com.e204.autocazing_alert.alert.controller;

import com.e204.autocazing_alert.alert.dto.AlertDetailsDto;
import com.e204.autocazing_alert.alert.service.AlertService;
import com.e204.autocazing_alert.alert.service.SseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {
    @Autowired
    private SseService sseService;
    @Autowired
    private AlertService alertService;


    @Operation(summary = "알림 구독 요청", description = "알림을 받기위해 연결해두는 API입니다. 로그인 후 바로 호출 해야함.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "구독 연결 성공",
                    content = @Content(mediaType = "application/json"
                    )
            )
    })
    //클라이언트에서 알림 수신하기 위해 연결해야함.
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String loginId = request.getHeader("loginId");
//        System.out.println("loginId: " + loginId);
        System.out.println("연결 완료!");

        // Set X-Accel-Buffering header to disable buffering for this response
        response.setHeader("X-Accel-Buffering", "no");
        SseEmitter emitter = sseService.createEmitter(loginId);
        return ResponseEntity.ok(emitter);
    }

    @Operation(summary = "로그인한 계정의 알림 전체 조회", description = "알림을 조회하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 전체 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AlertDetailsDto.class)))

            )
    })
    //알림 조회하기 feat Id
    @GetMapping("")
    public ResponseEntity findAllAlertsByLoginId(HttpServletRequest httpServletRequest){
        String loginId =httpServletRequest.getHeader("loginId");
        List<AlertDetailsDto> alertDetails = alertService.findAllAlert(loginId);
        return ResponseEntity.ok(alertDetails);

    }

}
