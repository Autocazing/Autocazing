package e204.autocazing.vendor.dto;

import e204.autocazing.db.entity.VendorEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorDto {

    private Integer vendorId;
    private String vendorName;
    private String vendorManager;
    private String vendorManagerContact;
    private String vendorDescription;

    //Entity > Dto 로 변환
    public static VendorDto fromEntity(VendorEntity entity) {
        return VendorDto.builder()
                .vendorId(entity.getVendorId())
                .vendorName(entity.getVendorName())
                .vendorManager(entity.getVendorManager())
                .vendorManagerContact(entity.getVendorManagerContact())
                .vendorDescription(entity.getVendorDescription())
                .build();
    }
}
