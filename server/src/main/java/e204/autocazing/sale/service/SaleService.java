package e204.autocazing.sale.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
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

		//더미데이터 기준
		// String dateTimeString = "2024-05-08 17:00:00";
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		// LocalDateTime baseDate = LocalDateTime.parse(dateTimeString, formatter);

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

	public Integer getSoldNumber() {
		LocalDate currentDay = LocalDate.from(LocalDateTime.now());
		return orderRepository.getSoldNumber(currentDay);
	}

	public Map<String, Double> getAvgSales() {
		List<Map<String, Object>> saleDtoList = new ArrayList<>();

		LocalDateTime startDate = LocalDateTime.now().minusMonths(1);
		LocalDateTime endDate = LocalDateTime.now();
		saleDtoList = orderRepository.getAvgSales(startDate, endDate);

		Map<String, List<Double>> salesByDay = new HashMap<>();
		for(Map<String, Object> record : saleDtoList) {
			String dayOfWeek = (String) record.get("dayOfWeek");
			Double totalSales = ((Number) record.get("totalSales")).doubleValue();

			salesByDay.computeIfAbsent(dayOfWeek, k -> new ArrayList<>()).add(totalSales);
		}

		Map<String, Double> salesAvgByDay = new HashMap<>();
		salesByDay.forEach((dayOfWeek, totalSales)->{
			salesAvgByDay.put(dayOfWeek, totalSales.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
		});

		return salesAvgByDay;
	}
}