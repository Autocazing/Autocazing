package e204.autocazing.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderSpecificEntity {

    @Column(nullable = false)
    private Integer menuId;
    @Column(nullable = false)
    private Integer menuQuantity;
    @Column(nullable = false)
    private Integer menuPrice;

    //연관
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id", nullable = false)
//    private OrderEntity orderEntity;

}
