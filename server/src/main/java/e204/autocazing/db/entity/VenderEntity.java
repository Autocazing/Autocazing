package e204.autocazing.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Venders")
public class VenderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer venderId;

    @Column(nullable = false)
    private String venderName;

    @Column(nullable = false)
    private String venderManager;

    @Column(nullable = false)
    private String venderManagerContact;

    @Column(nullable = false)
    private String venderDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    @OneToMany(mappedBy = "vender")
    private List<IngredientEntity> ingredients;
}
