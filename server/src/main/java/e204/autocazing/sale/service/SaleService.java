package e204.autocazing.sale.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import e204.autocazing.db.repository.OrderRepository;

@Service
public class SaleService {

	@Autowired
	private OrderRepository orderRepository;
	public List<Map<String, Object>> getSales(String type) {
		List<Map<String, Object>> saleDtoList = new ArrayList<>();

		if(type.equals("day")){
			LocalDateTime currentTime = LocalDateTime.now().minusDays(30);
			saleDtoList = orderRepository.calculateDailySales(currentTime);
		}
		else if(type.equals("week")){
			LocalDateTime currentTime = LocalDateTime.now().minusWeeks(12);
			saleDtoList = orderRepository.calculateWeekSales(currentTime);
		}
		else if(type.equals("month")){
			LocalDateTime currentTime = LocalDateTime.now().minusMonths(12);
			saleDtoList = orderRepository.calculateMonthSales(currentTime);
		}

		return saleDtoList;
	}
}