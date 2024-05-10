package e204.autocazing.vendor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchVendorDto {

    private String vendorName;
    private String vendorManager;
    private String vendorManagerContact;
    private String vendorDescription;

}