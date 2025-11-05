package edu.uoc.epcsd.digital.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataDigitalSessionRepository extends JpaRepository<DigitalSessionEntity, Long> {

	@Query("select a from digitalsession a where a.email = ?1 ")
	public List<DigitalSessionEntity> findDigitalSessionByUser(String email);
	
	//@Query("select a from DigitalSession a where a.id = ?1 ")
	public Optional<DigitalSessionEntity> getDigitalSessionById(Long id);
    
}
