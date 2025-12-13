package edu.uoc.epcsd.user.application.rest;

import edu.uoc.epcsd.user.application.rest.request.CreateRentalRequest;
import edu.uoc.epcsd.user.application.rest.request.RentalRequestProductRequest;
import edu.uoc.epcsd.user.domain.RentalRequest;
import edu.uoc.epcsd.user.domain.RentalRequestProduct;
import edu.uoc.epcsd.user.domain.service.RentalRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/rentalRequests")
public class RentalRequestRESTController {

    private final RentalRequestService rentalRequestService;

    @PostMapping
    public ResponseEntity<Long> createRentalRequest(@RequestBody @Valid CreateRentalRequest createRentalRequest) {
        log.trace("createRentalRequest");

        try {
            log.trace("Creating rental request " + createRentalRequest);
            List<RentalRequestProduct> products = createRentalRequest.getProducts().stream()
                    .map(p -> RentalRequestProduct.builder()
                            .productId(p.getProductId())
                            .units(p.getUnits())
                            .build())
                    .collect(Collectors.toList());

            Long rentalRequestId = rentalRequestService.createRentalRequest(RentalRequest.builder()
                    .userId(createRentalRequest.getUserId())
                    .startDate(createRentalRequest.getStartDate())
                    .endDate(createRentalRequest.getEndDate())
                    .products(products)
                    .build());
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(rentalRequestId)
                    .toUri();

            return ResponseEntity.created(uri).body(rentalRequestId);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified userId " + createRentalRequest.getUserId() + " does not exist.", e);
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One of the specified productIds does not exist.", e);
        }
    }

    @GetMapping
    public ResponseEntity<List<RentalRequest>> getAllRentalRequests() {
        log.trace("getAllRentalRequests");

        List<RentalRequest> rentalRequests = rentalRequestService.findAllRentalRequests();
        return ResponseEntity.ok(rentalRequests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalRequest> getRentalRequestById(@PathVariable Long id) {
        log.trace("getRentalRequestById: " + id);

        Optional<RentalRequest> rentalRequest = rentalRequestService.findRentalRequestById(id);
        if (rentalRequest.isPresent()) {
            return ResponseEntity.ok(rentalRequest.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
