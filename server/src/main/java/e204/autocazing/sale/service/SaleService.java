package e204.autocazing.sale.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import e204.autocazing.db.repository.OrderRepository;
import e204.autocazing.sale.dto.saleDto;

@Service
public class SaleService {

	private OrderRepository orderRepository;
	public List<saleDto> getSales(String type) {
		List<saleDto> saleDtoList = new ArrayList<>();

		if(type.equals("day")){
			LocalDateTime currentTime = LocalDateTime.now().minusDays(30);
			orderRepository.calculateSalesSince(currentTime);
		}
		else if(type.equals("week")){

		}
		else if(type.equals("month")){

		}

		return saleDtoList;
	}
}
