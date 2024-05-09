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
@Table(name = "Vendors")
public class VendorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vendorId;

    @Column(nullable = false)
    private String vendorName;

    @Column(nullable = false)
    private String vendorManager;

    @Column(nullable = false)
    private String vendorManagerContact;

    @Column(nullable = false)
    private String vendorDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    @OneToMany(mappedBy = "vendor")
    private List<IngredientEntity> ingredients;
}
