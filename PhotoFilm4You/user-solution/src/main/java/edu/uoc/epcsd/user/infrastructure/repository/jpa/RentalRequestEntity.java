package edu.uoc.epcsd.user.infrastructure.repository.jpa;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.uoc.epcsd.user.domain.RentalRequest;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "RentalRequest")
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalRequestEntity implements DomainTranslatable<RentalRequest> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false, columnDefinition = "DATE")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false, columnDefinition = "DATE")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    @OneToMany(mappedBy = "rentalRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RentalRequestProductEntity> products;

    @ManyToOne
    private UserEntity user;

    public static RentalRequestEntity fromDomain(RentalRequest rentalRequest) {
        if (rentalRequest == null) {
            return null;
        }

        RentalRequestEntity entity = RentalRequestEntity.builder()
                .id(rentalRequest.getId())
                .startDate(rentalRequest.getStartDate())
                .endDate(rentalRequest.getEndDate())
                .build();

        if (rentalRequest.getProducts() != null) {
            entity.setProducts(rentalRequest.getProducts().stream()
                    .map(product -> RentalRequestProductEntity.fromDomain(product, entity))
                    .collect(Collectors.toList()));
        }

        return entity;
    }

    @Override
    public RentalRequest toDomain() {
        return RentalRequest.builder()
                .id(this.getId())
                .startDate(this.getStartDate())
                .endDate(this.getEndDate())
                .userId(this.getUser().getId())
                .products(this.getProducts() != null ? this.getProducts().stream()
                        .map(RentalRequestProductEntity::toDomain)
                        .collect(Collectors.toList()) : null)
                .build();
    }
}
