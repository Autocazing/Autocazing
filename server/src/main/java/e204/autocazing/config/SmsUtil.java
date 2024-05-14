package e204.autocazing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

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
	public SingleMessageSentResponse sendOne(String to, String storeName ) {
		String restockOrderCheckLink = "발주 수락 링크"; //발주 전표 확인 링크
		String deliveryStartAPI = "배송 시작 링크"; //배송 시작
		String deliveryEndAPI = "배송 완료 링크"; //배송 완료

		Message message = new Message();
		// 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
		message.setFrom(sendNumber);
		message.setSubject("["+storeName+"]의 발주 요청\n");
		message.setTo(to);
		message.setText(
			"발주 전표 확인하기 : " + restockOrderCheckLink +"\n"
				+ "배송 시작 : " + deliveryStartAPI +"\n"
				+ "배송 완료 : " + deliveryEndAPI );

		SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
		return response;
	}
}
