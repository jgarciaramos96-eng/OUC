package edu.uoc.epcsd.user.application.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.uoc.epcsd.user.application.rest.request.LoginRequest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void builderSetsEmailAndPassword() {
        LoginRequest request = LoginRequest.builder()
                .email("user@example.com")
                .password("secret")
                .build();

        assertThat(request.getEmail()).isEqualTo("user@example.com");
        assertThat(request.getPassword()).isEqualTo("secret");
    }

    @Test
    void notBlankConstraintsAreValidated() {
        LoginRequest request = LoginRequest.builder()
                .email("")
                .password("")
                .build();

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertThat(violations)
                .hasSize(2)
                .extracting(ConstraintViolation::getPropertyPath)
                .extracting(Object::toString)
                .containsExactlyInAnyOrder("email", "password");

        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .allSatisfy(message -> assertThat(message).isNotBlank());
    }

    @Test
    void settersAndAllArgsConstructorCoverFields() {
        LoginRequest request = new LoginRequest();
        request.setEmail("setter@example.com");
        request.setPassword("setterpass");

        assertThat(request.getEmail()).isEqualTo("setter@example.com");
        assertThat(request.getPassword()).isEqualTo("setterpass");

        LoginRequest allArgs = new LoginRequest("user@example.com", "pass123");

        assertThat(allArgs.getEmail()).isEqualTo("user@example.com");
        assertThat(allArgs.getPassword()).isEqualTo("pass123");
    }

    @Test
    void dataGeneratedMethodsCoverEqualityAndToString() {
        LoginRequest request = new LoginRequest("user@example.com", "pass123");
        LoginRequest sameRequest = new LoginRequest("user@example.com", "pass123");
        LoginRequest differentRequest = new LoginRequest("other@example.com", "pass123");

        assertThat(request)
                .isEqualTo(sameRequest)
                .hasSameHashCodeAs(sameRequest)
                .isNotEqualTo(differentRequest)
                .doesNotHaveSameHashCodeAs(differentRequest);

        assertThat(request.toString())
                .contains("user@example.com")
                .contains("pass123");
    }
}