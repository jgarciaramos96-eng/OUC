package edu.uoc.epcsd.digital.domain.service;

import edu.uoc.epcsd.digital.domain.DigitalSession;
import edu.uoc.epcsd.digital.domain.DigitalStatus;
import edu.uoc.epcsd.digital.domain.exception.UserNotFoundException;
import edu.uoc.epcsd.digital.domain.repository.DigitalSessionRepository;
import edu.uoc.epcsd.digital.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class DigitalSessionServiceImpl implements DigitalSessionService {

    private final DigitalSessionRepository digitalSessionRepository;

    private final UserRepository userRepository;


    public List<DigitalSession> findAllDigitalSession() {
        return digitalSessionRepository.findAllDigitalSession();
    }

    @Override
    public List<DigitalSession> findDigitalSessionByUser(String email) {
		if (!userRepository.findUserByEmail(email)) {
            throw new UserNotFoundException(email);
        }

        return digitalSessionRepository.findDigitalSessionByUser(email);
    }

    @Override
    public Optional<DigitalSession> getDigitalSessionById(Long id) {
        return digitalSessionRepository.getDigitalSessionById(id);
    }

    @Override
    public Long createDigitalSession(DigitalSession digitalSession) {
        return digitalSessionRepository.createDigitalSession(digitalSession);
    }

    @Override
    public Long updateDigitalSession(Long digitalSessionId, String email, String description) {
        DigitalSession digitalSession = digitalSessionRepository
                .getDigitalSessionById(digitalSessionId)
                .orElseThrow(IllegalArgumentException::new);

        digitalSession.setEmail(email);        
        digitalSession.setDescription(description);
        digitalSession.setStatus(DigitalStatus.NOT_AVAILABLE);

        return digitalSessionRepository.updateDigitalSession(digitalSession);
    }

    @Override
    public void removeDigitalSession(Long id) {
        DigitalSession digitalSession = digitalSessionRepository
                .getDigitalSessionById(id)
                .orElseThrow(IllegalArgumentException::new);
        digitalSessionRepository.removeDigitalSession(digitalSession);
    }

}
