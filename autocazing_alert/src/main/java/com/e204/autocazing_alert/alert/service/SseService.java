package com.e204.autocazing_alert.alert.service;

import com.e204.autocazing_alert.db.entity.AlertEntity;
import com.e204.autocazing_alert.db.repository.AlertRepository;
import com.e204.autocazing_alert.kafka.entity.IngredientWarnEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseService {
    private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Autowired
    private AlertRepository alertRepository;

    //Emitter 객체 생성 후 연결이 오나료되거나 타임아웃될때 리스트에서 제거
    //Long.MAX_VALUE 는 사실상 무한대
    public SseEmitter createEmitter(String loginId) throws IOException {
//        SseEmitter emitter = new SseEmitter(1000L * 60 * 60 * 24);
        SseEmitter emitter =new SseEmitter(Long.MAX_VALUE);
        this.emitters.put(loginId,emitter);

        emitter.send(SseEmitter.event()
                .name("connect")         // 해당 이벤트의 이름 지정
                .data("connected!"));    // 503 에러 방지를 위한 더미 데이터

        emitter.onCompletion(() -> {
            this.emitters.remove(loginId);    // 만료되면 리스트에서 삭제
        });

        emitter.onTimeout(() -> {
            this.emitters.remove(loginId);    // 타임아웃되면 리스트에서 삭제
        });

        emitter.onError((e) -> {
            this.emitters.remove(loginId);    // 에러 발생 시 리스트에서 삭제
        });

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("SIZE : " + emitters.size());
        return emitter;
    }

    //발주 시 보내는 알림
    public void sendRestockNotification(String loginId, IngredientWarnEntity ingredientWarnEntity) {
        SseEmitter emitter = emitters.get(loginId);

        System.out.println("loginId 로 가져온 emitter 로 알림~" + loginId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("restock").data(ingredientWarnEntity));
                System.out.println("restock 알림 보냈슈");
                AlertEntity alertEntity = new AlertEntity();
                alertEntity.setContent(ingredientWarnEntity);
                alertEntity.setCompleted(false);
                alertEntity.setLoginId(loginId);
                alertRepository.save(alertEntity);
            } catch (IOException e) {
                this.emitters.remove(loginId);
               // emitter.completeWithError(e);

            }
        }
    }


    //배송 상태 변경시 보내는 알림
    public void sendDeliveringNotification(String loginId, String message) {
        SseEmitter emitter = emitters.get(loginId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("delivering").data(message));
            } catch (IOException e) {
                this.emitters.remove(loginId);
                //emitter.completeWithError(e);
            }
        }
    }

    //매출 변경(메뉴 팔았을때) 보내는 알림
    public void sendSalesNotification(String loginId, String message) {
        SseEmitter emitter = emitters.get(loginId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("sales").data(message));
            } catch (IOException e) {
                this.emitters.remove(loginId);
                //emitter.completeWithError(e);
            }
        }
    }
}

