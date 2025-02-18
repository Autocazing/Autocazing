package e204.autocazing.db.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "menu_ingredient")
@IdClass(MenuIngredientId.class) //복합 키 클래스를 지정하기.
public class MenuIngredientEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private MenuEntity menu;

    @Id
    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private IngredientEntity ingredient;

    @Column(nullable = false)
    private Integer capacity; //메뉴에 들어가는 재료의 양


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //vender는 굳이..?
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "vender_id", referencedColumnName = "venderId")
//    private VenderEntity vender;

}
