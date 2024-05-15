package e204.autocazing.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

import e204.autocazing.restock.dto.SmsRequestDto;
import jakarta.annotation.PostConstruct;

@Component
public class SmsUtil {
	@Value("${coolsms.api.key}")
	private String apiKey;
	@Value("${coolsms.api.secret}")
	private String apiSecretKey;
	@Value("${sendNumber}")
	private String sendNumber;

	private DefaultMessageService messageService;

	@PostConstruct
	private void init(){
		this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
	}

	// 단일 메시지 발송 예제
	public List<SingleMessageSentResponse> sendOne(List<SmsRequestDto> orderList, Integer restockOrderId,String storeName ) {
		String deliveryStartAPI = "https://k10e204.p.ssafy.io/api/restocks/"+restockOrderId+"/start"; //배송 시작
		String deliveryEndAPI = "https://k10e204.p.ssafy.io/api/restocks/"+restockOrderId+"/arrive"; //배송 완료

		List<SingleMessageSentResponse> responses = new ArrayList<>();

		for (SmsRequestDto order : orderList) {
			// String to = order.getVenderManagerContact();
			//나중에 업체 번호 변경 해야 함.
			String to = sendNumber;

			Message message = new Message();
			message.setFrom(sendNumber);
			message.setSubject("[" + storeName + "] 발주 요청\n");
			message.setTo(to);

			StringBuilder text = new StringBuilder();
			text.append("[ 주문 목록 ]\n");

			for (Map<String, Integer> orderItem : order.getOrderList()) {
				for (Map.Entry<String, Integer> entry : orderItem.entrySet()) {
					text.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
				}
			}
			text.append("[ 배송 시작 ]\n").append(deliveryStartAPI).append("\n")
				.append("[ 배송 완료 ]\n").append(deliveryEndAPI).append("\n");

			message.setText(text.toString());

			SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

			//시연할 때 주석 해제 해야 합니다!
			responses.add(response);
		}
		return responses;
	}
}