package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.exception.ProductNotFoundException;
import edu.uoc.epcsd.user.domain.repository.ProductRepository;
import edu.uoc.epcsd.user.domain.RentalRequest;
import edu.uoc.epcsd.user.domain.repository.RentalRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class RentalRequestServiceImpl implements RentalRequestService {

    private final RentalRequestRepository rentalRequestRepository;

    private final ProductRepository productRepository;

    @Override
    public List<RentalRequest> findAllRentalRequests() {
        return rentalRequestRepository.findAllRentalRequests();
    }

    @Override
    public Optional<RentalRequest> findRentalRequestById(Long id) {
        return rentalRequestRepository.findRentalRequestById(id);
    }

    @Override
    public Long createRentalRequest(RentalRequest rentalRequest) throws ProductNotFoundException {
        for (var product : rentalRequest.getProducts()) {
            if (!productRepository.existsById(product.getProductId())) {
                throw new ProductNotFoundException(product.getProductId());
            }
        }
        return rentalRequestRepository.createRentalRequest(rentalRequest);
    }
}
