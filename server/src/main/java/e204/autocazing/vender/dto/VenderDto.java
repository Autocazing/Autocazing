package e204.autocazing.vender.dto;

import e204.autocazing.db.entity.VenderEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenderDto {

    private Integer vendorId;
    private String vendorName;
    private String vendorManager;
    private String vendorManagerContact;
    private String vendorDescription;

    //Entity > Dto 로 변환
    public static VenderDto fromEntity(VenderEntity entity) {
        return VenderDto.builder()
                .vendorId(entity.getVenderId())
                .vendorName(entity.getVenderName())
                .vendorManager(entity.getVenderManager())
                .vendorManagerContact(entity.getVenderManagerContact())
                .vendorDescription(entity.getVenderDescription())
                .build();
    }
}
