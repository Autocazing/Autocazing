package com.e204.autocazing_alert.alert.controller;

import com.e204.autocazing_alert.alert.service.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/alert")
public class AlertController {
    @Autowired
    private SseService sseService;

    //클라이언트에서 알림 수신하기 위해 연결해야함.
    @GetMapping("")
    public SseEmitter subscribe() {
        return sseService.createEmitter();
    }
}
