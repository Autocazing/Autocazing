package e204.autocazing.vender.dto;

import e204.autocazing.db.entity.VenderEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenderDto {

    private Integer venderId;
    private String venderName;
    private String venderManager;
    private String venderManagerContact;
    private String venderDescription;

    //Entity > Dto 로 변환
    public static VenderDto fromEntity(VenderEntity entity) {
        return VenderDto.builder()
                .venderId(entity.getVenderId())
                .venderName(entity.getVenderName())
                .venderManager(entity.getVenderManager())
                .venderManagerContact(entity.getVenderManagerContact())
                .venderDescription(entity.getVenderDescription())
                .build();
    }
}
