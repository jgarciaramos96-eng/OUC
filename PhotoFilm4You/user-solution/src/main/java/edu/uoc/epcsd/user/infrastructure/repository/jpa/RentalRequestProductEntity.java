package edu.uoc.epcsd.user.infrastructure.repository.jpa;

import edu.uoc.epcsd.user.domain.RentalRequestProduct;
import lombok.*;

import javax.persistence.*;

@Entity(name = "RentalRequestProduct")
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalRequestProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "productId", nullable = false)
    private Long productId;

    @Column(name = "units", nullable = false)
    private Integer units;

    @ManyToOne
    @JoinColumn(name = "rental_request_id", nullable = false)
    private RentalRequestEntity rentalRequest;

    public static RentalRequestProductEntity fromDomain(RentalRequestProduct product, RentalRequestEntity rentalRequest) {
        if (product == null) {
            return null;
        }

        return RentalRequestProductEntity.builder()
                .productId(product.getProductId())
                .units(product.getUnits())
                .rentalRequest(rentalRequest)
                .build();
    }

    public RentalRequestProduct toDomain() {
        return RentalRequestProduct.builder()
                .productId(this.getProductId())
                .units(this.getUnits())
                .build();
    }
}
