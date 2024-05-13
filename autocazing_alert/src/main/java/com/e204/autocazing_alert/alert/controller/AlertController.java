package com.e204.autocazing_alert.alert.controller;

import com.e204.autocazing_alert.alert.dto.AlertDetailsDto;
import com.e204.autocazing_alert.alert.service.AlertService;
import com.e204.autocazing_alert.alert.service.SseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/alert")
public class AlertController {
    @Autowired
    private SseService sseService;
    @Autowired
    private AlertService alertService;


    //클라이언트에서 알림 수신하기 위해 연결해야함.
    @GetMapping("")
    public SseEmitter subscribe(HttpServletRequest httpServletRequest) {
        String loginId = httpServletRequest.getHeader("loginId");

        return sseService.createEmitter(loginId);
    }

    //알림 조회하기 feat Id
    @GetMapping("/{loginId}")
    public ResponseEntity findAllAlertsByLoginId(HttpServletRequest httpServletRequest){
        String loginId = httpServletRequest.getHeader("loginId");
        List<AlertDetailsDto> alertDetails = alertService.findAllAlert(loginId);
        return ResponseEntity.ok(alertDetails);

    }

    //테스트용 알림
//    @GetMapping("/tset")
//    public ResponseEntity TestAlertService(HttpServletRequest httpServletRequest)
}
