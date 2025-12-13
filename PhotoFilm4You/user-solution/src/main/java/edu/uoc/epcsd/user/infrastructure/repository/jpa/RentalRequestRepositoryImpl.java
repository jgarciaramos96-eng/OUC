package edu.uoc.epcsd.user.infrastructure.repository.jpa;

import edu.uoc.epcsd.user.domain.RentalRequest;
import edu.uoc.epcsd.user.domain.repository.RentalRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RentalRequestRepositoryImpl implements RentalRequestRepository {

    private final SpringDataRentalRequestRepository jpaRepository;

    private final SpringDataUserRepository jpaUserRepository;

    @Override
    public List<RentalRequest> findAllRentalRequests() {
        return jpaRepository.findAll().stream().map(RentalRequestEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<RentalRequest> findRentalRequestById(Long id) {
        return jpaRepository.findById(id).map(RentalRequestEntity::toDomain);
    }

    @Override
    public Long createRentalRequest(RentalRequest rentalRequest) {
        UserEntity userEntity = jpaUserRepository.findById(rentalRequest.getUserId()).orElseThrow(IllegalArgumentException::new);

        RentalRequestEntity rentalRequestEntity = RentalRequestEntity.fromDomain(rentalRequest);
        rentalRequestEntity.setUser(userEntity);

        return jpaRepository.save(rentalRequestEntity).getId();
    }
}
