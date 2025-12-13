package edu.uoc.epcsd.user.infrastructure.repository.jpa;

import edu.uoc.epcsd.user.domain.RentalRequest;
import edu.uoc.epcsd.user.domain.RentalRequestProduct;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RentalRequestEntityTest {

    @Test
    void fromDomainReturnsNullWhenDomainIsNull() {
        RentalRequestEntity entity = RentalRequestEntity.fromDomain(null);
        assertNull(entity);
    }

    @Test
    void fromDomainMapsAllFieldsCorrectly() {
        RentalRequest domain = RentalRequest.builder()
                .id(1L)
                .startDate(LocalDate.of(2025, 1, 10))
                .endDate(LocalDate.of(2025, 1, 12))
                .userId(50L)
                .build();

        RentalRequestEntity entity = RentalRequestEntity.fromDomain(domain);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals(domain.getStartDate(), entity.getStartDate());
        assertEquals(domain.getEndDate(), entity.getEndDate());
    }

    @Test
    void fromDomainMapsProductsAndSetsBackReference() {
        RentalRequest domain = RentalRequest.builder()
                .id(2L)
                .startDate(LocalDate.of(2025, 3, 1))
                .endDate(LocalDate.of(2025, 3, 3))
                .userId(99L)
                .products(List.of(
                        RentalRequestProduct.builder()
                                .productId(100L)
                                .units(2)
                                .build()
                ))
                .build();

        RentalRequestEntity entity = RentalRequestEntity.fromDomain(domain);

        assertNotNull(entity.getProducts());
        assertEquals(1, entity.getProducts().size());
        assertEquals(100L, entity.getProducts().get(0).getProductId());
        assertEquals(2, entity.getProducts().get(0).getUnits());
        assertSame(entity, entity.getProducts().get(0).getRentalRequest());
    }

    @Test
    void toDomainMapsAllFieldsCorrectly() {
        UserEntity user = new UserEntity();
        user.setId(77L);

        RentalRequestProductEntity productEntity = new RentalRequestProductEntity();
        productEntity.setProductId(300L);
        productEntity.setUnits(4);

        RentalRequestEntity entity = new RentalRequestEntity();
        entity.setId(9L);
        entity.setStartDate(LocalDate.of(2025, 5, 1));
        entity.setEndDate(LocalDate.of(2025, 5, 2));
        entity.setUser(user);
        entity.setProducts(List.of(productEntity));

        RentalRequest domain = entity.toDomain();

        assertNotNull(domain);
        assertEquals(9L, domain.getId());
        assertEquals(77L, domain.getUserId());
        assertEquals(LocalDate.of(2025, 5, 1), domain.getStartDate());
        assertEquals(LocalDate.of(2025, 5, 2), domain.getEndDate());

        assertNotNull(domain.getProducts());
        assertEquals(1, domain.getProducts().size());
        assertEquals(300L, domain.getProducts().get(0).getProductId());
        assertEquals(4, domain.getProducts().get(0).getUnits());
    }

    @Test
    void toDomainHandlesNullProducts() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        RentalRequestEntity entity = new RentalRequestEntity();
        entity.setId(10L);
        entity.setStartDate(LocalDate.of(2025, 6, 1));
        entity.setEndDate(LocalDate.of(2025, 6, 2));
        entity.setUser(user);
        entity.setProducts(null);

        RentalRequest domain = entity.toDomain();

        assertNotNull(domain);
        assertNull(domain.getProducts());
    }
}
