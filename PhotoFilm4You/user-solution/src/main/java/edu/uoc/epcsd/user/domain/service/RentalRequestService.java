package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.RentalRequest;

import java.util.List;
import java.util.Optional;

public interface RentalRequestService {

    List<RentalRequest> findAllRentalRequests();

    Optional<RentalRequest> findRentalRequestById(Long id);

    Long createRentalRequest(RentalRequest rentalRequest);

}
