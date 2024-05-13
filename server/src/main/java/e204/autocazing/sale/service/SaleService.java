package e204.autocazing.sale.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import e204.autocazing.db.repository.OrderRepository;
import e204.autocazing.db.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SaleService {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private StoreRepository storeRepository;

	public List<Map<String, Object>> getSales(String type, String loginId) {
		List<Map<String, Object>> saleDtoList = new ArrayList<>();
		LocalDateTime currentTime = LocalDateTime.now();
		log.info("SaleService_loginId : "+loginId);
		Integer storeId = storeRepository.findByLoginId(loginId);

		if (type.equals("day")) {
			LocalDateTime startTime = currentTime.minusDays(30);
			log.info("SaleService_storeId : "+storeId);
			saleDtoList = orderRepository.calculateDailySales(startTime, storeId);

			fillMissingDays(saleDtoList, startTime.toLocalDate(), LocalDate.now());
			Collections.sort(saleDtoList, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					LocalDate date1 = ((Date) o1.get("date")).toLocalDate();
					LocalDate date2 = ((Date) o2.get("date")).toLocalDate();
					return date1.compareTo(date2);
				}
			});

		} else if (type.equals("week")) {
			LocalDateTime startTime = currentTime.minusWeeks(12);
			saleDtoList = orderRepository.calculateWeekSales(startTime, storeId);
			fillMissingWeeks(saleDtoList, currentTime);

			Collections.sort(saleDtoList, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					Integer year1 = (Integer) o1.get("year");
					Integer week1 = (Integer) o1.get("week");
					Integer year2 = (Integer) o2.get("year");
					Integer week2 = (Integer) o2.get("week");
					int yearCompare = year1.compareTo(year2);
					if (yearCompare == 0) {
						return week1.compareTo(week2);
					}
					return yearCompare;
				}
			});

		} else if (type.equals("month")) {
			LocalDateTime startTime = currentTime.minusMonths(12);
			saleDtoList = orderRepository.calculateMonthSales(startTime, storeId);
			fillMissingMonths(saleDtoList, currentTime);

			Collections.sort(saleDtoList, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					Integer year1 = (Integer) o1.get("year");
					Integer month1 = (Integer) o1.get("month");
					Integer year2 = (Integer) o2.get("year");
					Integer month2 = (Integer) o2.get("month");
					int yearCompare = year1.compareTo(year2);
					if (yearCompare == 0) {
						return month1.compareTo(month2);
					}
					return yearCompare;
				}
			});

		}
		return saleDtoList;
	}
	private void fillMissingDays(List<Map<String, Object>> sales, LocalDate start, LocalDate end) {
		Set<LocalDate> existingDates = sales.stream()
			.map(s -> ((Date) s.get("date")).toLocalDate())
			.collect(Collectors.toSet());
		for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
			if (!existingDates.contains(date)) {
				Map<String, Object> missingDay = new HashMap<>();
				missingDay.put("date", Date.valueOf(date));
				missingDay.put("totalSales", 0);
				sales.add(missingDay);
			}
		}
	}

	private void fillMissingWeeks(List<Map<String, Object>> sales, LocalDateTime currentTime) {
		Set<String> existingWeeks = sales.stream()
			.map(s -> s.get("year") + "-" + s.get("week"))
			.collect(Collectors.toSet());

		int currentYear = currentTime.getYear();
		int currentWeek = currentTime.get(WeekFields.ISO.weekOfWeekBasedYear());

		for (int i = 0; i < 12; i++) {
			if (!existingWeeks.contains(currentYear + "-" + currentWeek)) {
				Map<String, Object> missingWeek = new HashMap<>();
				missingWeek.put("week", currentWeek);
				missingWeek.put("year", currentYear);
				missingWeek.put("totalSales", 0);
				sales.add(missingWeek);
			}
			currentTime = currentTime.minusWeeks(1);
			currentWeek = currentTime.get(WeekFields.ISO.weekOfWeekBasedYear());
			currentYear = currentTime.getYear();
		}
	}


	private void fillMissingMonths(List<Map<String, Object>> sales, LocalDateTime currentTime) {
		Set<String> existingMonths = sales.stream()
			.map(s -> s.get("year") + "-" + s.get("month"))
			.collect(Collectors.toSet());

		int currentYear = currentTime.getYear();
		int currentMonth = currentTime.getMonthValue();

		for (int i = 0; i < 12; i++) {
			if (!existingMonths.contains(currentYear + "-" + currentMonth)) {
				Map<String, Object> missingMonth = new HashMap<>();
				missingMonth.put("month", currentMonth);
				missingMonth.put("year", currentYear);
				missingMonth.put("totalSales", 0);
				sales.add(missingMonth);
			}
			currentTime = currentTime.minusMonths(1);
			currentMonth = currentTime.getMonthValue();
			currentYear = currentTime.getYear();
		}
	}

	public Integer getSoldNumber(String loginId) {
		Integer storeId = storeRepository.findByLoginId(loginId);
		LocalDate currentDay = LocalDate.from(LocalDateTime.now());
		return orderRepository.getSoldNumber(currentDay, storeId);
	}

	public Map<String, Double> getAvgSales(String loginId) {
		Integer storeId = storeRepository.findByLoginId(loginId);
		List<Map<String, Object>> saleDtoList = new ArrayList<>();

		LocalDateTime startDate = LocalDateTime.now().minusMonths(1);
		LocalDateTime endDate = LocalDateTime.now();
		saleDtoList = orderRepository.getAvgSales(startDate, endDate, storeId);

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
