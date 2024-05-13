package com.e204.autocazing_alert.alert.service;


import com.e204.autocazing_alert.alert.dto.AddSpecificRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class AlertService {
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter createEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        this.emitters.add(emitter);

        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));

        return emitter;
    }
}
//
//    public void sendNotificationToAll(AddSpecificRequest addSpecificRequest) {
//        for (SseEmitter emitter : this.emitters) {
//            try {
//                emitter.send(SseEmitter.event()
//                        .name("restock")
//                        .data(notification, MediaType.APPLICATION_JSON));
//            } catch (IOException e) {
//                emitter.completeWithError(e);
//            }
//        }
//}
