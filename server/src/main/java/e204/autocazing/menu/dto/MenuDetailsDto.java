package e204.autocazing.menu.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuDetailsDto {
    private Integer menuId;
    private String menuName;
    private Integer menuPrice;
    private Boolean onEvent;
}
