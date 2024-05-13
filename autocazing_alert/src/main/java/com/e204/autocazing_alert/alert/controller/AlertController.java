package com.e204.autocazing_alert.alert.controller;

import com.e204.autocazing_alert.alert.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/alert")
public class AlertController {
    @Autowired
    private AlertService alertService;

    //SSE 연결하는 API
    @GetMapping("")
    public SseEmitter subscribe() {
        return alertService.createEmitter();
    }
}
