package edu.uoc.epcsd.user.infrastructure.repository.jpa;

import edu.uoc.epcsd.user.domain.RentalRequestProduct;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RentalRequestProductEntityTest {

    @Test
    void fromDomainReturnsEntityWhenProductIsNotNull() {
        // given
        RentalRequestProduct domain = RentalRequestProduct.builder()
                .productId(100L)
                .units(3)
                .build();

        RentalRequestEntity parent = new RentalRequestEntity();
        parent.setId(1L);

        RentalRequestProductEntity entity =
                RentalRequestProductEntity.fromDomain(domain, parent);

        assertNotNull(entity);
        assertEquals(100L, entity.getProductId());
        assertEquals(3, entity.getUnits());
        assertSame(parent, entity.getRentalRequest());
    }

    @Test
    void fromDomainReturnsNullWhenProductIsNull() {
        RentalRequestProductEntity entity =
                RentalRequestProductEntity.fromDomain(null, null);

        assertNull(entity);
    }

    @Test
    void toDomainReturnsDomainObject() {
        RentalRequestProductEntity entity = new RentalRequestProductEntity();
        entity.setProductId(200L);
        entity.setUnits(4);

        RentalRequestProduct domain = entity.toDomain();

        assertNotNull(domain);
        assertEquals(200L, domain.getProductId());
        assertEquals(4, domain.getUnits());
    }
}
