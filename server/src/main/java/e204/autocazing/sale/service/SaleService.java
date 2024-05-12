package e204.autocazing.sale.service;

import java.sql.Date;
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
		LocalDateTime currentTime = LocalDateTime.now();

		Map<String, Object> todaySales = new HashMap<>();
		todaySales.put("date", LocalDate.from(currentTime));
		todaySales.put("totalSales", 0);

		if(type.equals("day")){
			LocalDateTime startTime = currentTime.minusDays(30);
			saleDtoList = orderRepository.calculateDailySales(startTime);

			Date sqlLastSales = (Date)saleDtoList.get(saleDtoList.size()-1).get("date");
			LocalDate lastSales = sqlLastSales.toLocalDate();
			if(!lastSales.equals(currentTime.toLocalDate()))
				saleDtoList.add(todaySales);
		}
		else if(type.equals("week")){
			LocalDateTime startTime = currentTime.minusWeeks(12);
			saleDtoList = orderRepository.calculateWeekSales(startTime);
		}
		else if(type.equals("month")){
			LocalDateTime startTime = currentTime.minusMonths(12);
			saleDtoList = orderRepository.calculateMonthSales(startTime);
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