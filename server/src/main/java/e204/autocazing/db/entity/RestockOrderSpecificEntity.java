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
public class RestockOrderSpecificEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer restockOrderSpecificId;

    @Column(nullable = false)
    private Integer ingredientQuantity;

    @Column(nullable = false)
    private Integer ingredientPrice;

    //연관
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "restock_order_id")
//    private RestockOrderEntity restockOrder;
}
