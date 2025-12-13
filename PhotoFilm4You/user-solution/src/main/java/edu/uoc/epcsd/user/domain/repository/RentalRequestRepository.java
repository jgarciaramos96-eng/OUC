package edu.uoc.epcsd.user.domain.repository;

import edu.uoc.epcsd.user.domain.RentalRequest;

import java.util.List;
import java.util.Optional;

public interface RentalRequestRepository {

    List<RentalRequest> findAllRentalRequests();

    Optional<RentalRequest> findRentalRequestById(Long id);

    Long createRentalRequest(RentalRequest rentalRequest);

}
