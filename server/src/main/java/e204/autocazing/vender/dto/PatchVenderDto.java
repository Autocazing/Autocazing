package e204.autocazing.vender.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchVenderDto {

    private String venderName;
    private String venderManager;
    private String venderManagerContact;
    private String venderDescription;

}