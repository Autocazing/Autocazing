package e204.autocazing.restock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import e204.autocazing.db.entity.RestockOrderSpecificEntity;
import e204.autocazing.restock.dto.UpdatedRestockSpecificDto;
import e204.autocazing.restockSpecific.service.RestockSpecificService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/sms")
public class SmsController {
	@Autowired
	private RestockSpecificService restockSpecificService;

	@GetMapping("/{restockOrderId}/{venderId}/start")
	public ResponseEntity<List<UpdatedRestockSpecificDto>> restockOrderStart(@PathVariable(name = "restockOrderId") Integer restockOrderId,
		@PathVariable(name = "venderId") Integer venderId,
		HttpServletRequest httpServletRequest) {

		//restockOrderId의 venderId가 같은 orderSpecific의 status를 ON_DELIVERY로 변경
		List<UpdatedRestockSpecificDto> updatedRestockSpecificDtoList
			= restockSpecificService.updateRestockOrderSpecificStatus(restockOrderId, venderId, RestockOrderSpecificEntity.RestockSpecificStatus.ON_DELIVERY);

		return ResponseEntity.ok(updatedRestockSpecificDtoList);
	}

	@GetMapping("/{restockOrderId}/{venderId}/arrive")
	public ResponseEntity<List<UpdatedRestockSpecificDto>> restockOrderArrive(@PathVariable(name = "restockOrderId") Integer restockOrderId,
		@PathVariable(name = "venderId") Integer venderId,
		HttpServletRequest httpServletRequest) {

		//restockOrderId의 venderId가 같은 orderSpecific의 status를 ARRIVED로 변경
		List<UpdatedRestockSpecificDto> updatedRestockSpecificDtoList
			= restockSpecificService.updateRestockOrderSpecificStatus(restockOrderId, venderId, RestockOrderSpecificEntity.RestockSpecificStatus.ARRIVED);

		return ResponseEntity.ok(updatedRestockSpecificDtoList);
	}
}
