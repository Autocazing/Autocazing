package e204.autocazing.restock.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SmsRequestDto {
	private String venderManagerContact;
	private List<Map<String, Integer>> orderList;
}
