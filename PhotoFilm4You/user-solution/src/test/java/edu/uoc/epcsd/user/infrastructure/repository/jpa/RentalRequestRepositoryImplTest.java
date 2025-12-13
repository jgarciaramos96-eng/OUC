package edu.uoc.epcsd.user.infrastructure.repository.jpa;

import edu.uoc.epcsd.user.domain.RentalRequest;
import edu.uoc.epcsd.user.domain.RentalRequestProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RentalRequestRepositoryImplTest {

    @Mock
    private SpringDataRentalRequestRepository jpaRepository;

    @Mock
    private SpringDataUserRepository jpaUserRepository;

    private RentalRequestRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new RentalRequestRepositoryImpl(jpaRepository, jpaUserRepository);
    }

    private RentalRequest buildDomainRentalRequest() {
        return RentalRequest.builder()
                .userId(10L)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(2))
                .products(List.of(
                        RentalRequestProduct.builder()
                                .productId(100L)
                                .units(1)
                                .build()
                ))
                .build();
    }

    private RentalRequestEntity buildEntity(Long id) {
        RentalRequestEntity entity = new RentalRequestEntity();
        entity.setId(id);
        entity.setStartDate(LocalDate.now());
        entity.setEndDate(LocalDate.now().plusDays(2));

        UserEntity user = new UserEntity();
        user.setId(10L);
        entity.setUser(user);

        entity.setProducts(List.of());
        return entity;
    }

    private UserEntity buildUserEntity() {
        UserEntity user = new UserEntity();
        user.setId(10L);
        return user;
    }

    @Test
    void findAllRentalRequestsReturnsMappedDomainObjects() {
        when(jpaRepository.findAll())
                .thenReturn(List.of(buildEntity(1L)));

        List<RentalRequest> result = repository.findAllRentalRequests();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(10L, result.get(0).getUserId());

        verify(jpaRepository).findAll();
    }

    @Test
    void findRentalRequestByIdReturnsDomainObject() {
        when(jpaRepository.findById(1L))
                .thenReturn(Optional.of(buildEntity(1L)));

        Optional<RentalRequest> result = repository.findRentalRequestById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(jpaRepository).findById(1L);
    }

    @Test
    void findRentalRequestByIdReturnsEmptyOptional() {
        when(jpaRepository.findById(1L))
                .thenReturn(Optional.empty());

        Optional<RentalRequest> result = repository.findRentalRequestById(1L);

        assertTrue(result.isEmpty());
        verify(jpaRepository).findById(1L);
    }

    @Test
    void createRentalRequestReturnsIdWhenUserExists() {
        RentalRequest request = buildDomainRentalRequest();
        UserEntity userEntity = buildUserEntity();
        RentalRequestEntity savedEntity = buildEntity(99L);

        when(jpaUserRepository.findById(10L)).thenReturn(Optional.of(userEntity));
        when(jpaRepository.save(any(RentalRequestEntity.class))).thenReturn(savedEntity);

        Long result = repository.createRentalRequest(request);

        assertEquals(99L, result);
        verify(jpaUserRepository).findById(10L);
        verify(jpaRepository).save(any(RentalRequestEntity.class));
    }

    @Test
    void createRentalRequestThrowsExceptionWhenUserNotFound() {
        RentalRequest request = buildDomainRentalRequest();

        when(jpaUserRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                repository.createRentalRequest(request)
        );

        verify(jpaUserRepository).findById(10L);
        verify(jpaRepository, never()).save(any());
    }
}
