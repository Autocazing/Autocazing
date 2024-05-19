package e204.autocazing.vender.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostVenderDto {
    private Integer storeId;
    private String vendorName;
    private String vendorManager;
    private String vendorManagerContact;
    private String vendorDescription;

}
