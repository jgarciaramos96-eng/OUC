package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.RentalRequest;
import edu.uoc.epcsd.user.domain.RentalRequestProduct;
import edu.uoc.epcsd.user.domain.exception.ProductNotFoundException;
import edu.uoc.epcsd.user.domain.repository.ProductRepository;
import edu.uoc.epcsd.user.domain.repository.RentalRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RentalRequestServiceImplTest {

    @Mock
    private RentalRequestRepository rentalRequestRepository;

    @Mock
    private ProductRepository productRepository;

    private RentalRequestServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new RentalRequestServiceImpl(rentalRequestRepository, productRepository);
    }

    private RentalRequest buildRentalRequest() {
        return RentalRequest.builder()
                .id(1L)
                .userId(10L)
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 1, 3))
                .products(List.of(
                        RentalRequestProduct.builder()
                                .productId(100L)
                                .units(2)
                                .build()
                ))
                .build();
    }


    @Test
    void findAllRentalRequestsReturnsList() {
        when(rentalRequestRepository.findAllRentalRequests())
                .thenReturn(List.of(buildRentalRequest()));

        List<RentalRequest> result = service.findAllRentalRequests();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(rentalRequestRepository).findAllRentalRequests();
    }


    @Test
    void findRentalRequestByIdReturnsOptionalWithValue() {
        when(rentalRequestRepository.findRentalRequestById(1L))
                .thenReturn(Optional.of(buildRentalRequest()));

        Optional<RentalRequest> result = service.findRentalRequestById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(rentalRequestRepository).findRentalRequestById(1L);
    }


    @Test
    void createRentalRequestReturnsIdWhenAllProductsExist() throws Exception {
        RentalRequest request = buildRentalRequest();

        when(productRepository.existsById(100L)).thenReturn(true);
        when(rentalRequestRepository.createRentalRequest(any(RentalRequest.class))).thenReturn(99L);

        Long result = service.createRentalRequest(request);

        assertEquals(99L, result);
        verify(productRepository).existsById(100L);
        verify(rentalRequestRepository).createRentalRequest(request);
    }

    @Test
    void createRentalRequestThrowsProductNotFoundException() {
        RentalRequest request = buildRentalRequest();

        when(productRepository.existsById(100L)).thenReturn(false);

   assertThrows(ProductNotFoundException.class,
                () -> service.createRentalRequest(request)
        );

        verify(productRepository).existsById(100L);
        verify(rentalRequestRepository, never()).createRentalRequest(any());
    }
}
