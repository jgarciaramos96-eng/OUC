package edu.uoc.epcsd.user.application.rest;

import edu.uoc.epcsd.user.application.rest.response.LoginResponse;
import edu.uoc.epcsd.user.domain.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginResponseTest {

    @Test
    void fromDomainCopiesUserFields() {
        User user = User.builder()
                .id(7L)
                .fullName("Grace Hopper")
                .email("grace@example.com")
                .phoneNumber("111-222-333")
                .password("secret")
                .build();

        LoginResponse response = LoginResponse.fromDomain(user);

        assertThat(response.getId()).isEqualTo(user.getId());
        assertThat(response.getFullName()).isEqualTo(user.getFullName());
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        assertThat(response.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
    }

    @Test
    void builderAllowsSettingToken() {
        LoginResponse response = LoginResponse.builder()
                .id(5L)
                .fullName("Ada Lovelace")
                .email("ada@example.com")
                .phoneNumber("000-111-222")
                .token("jwt.token")
                .build();

        assertThat(response.getToken()).isEqualTo("jwt.token");
    }

    @Test
    void noArgsConstructorAndSettersPopulateFields() {
        LoginResponse response = new LoginResponse();

        response.setId(9L);
        response.setFullName("Katherine Johnson");
        response.setEmail("kj@example.com");
        response.setPhoneNumber("123-456-789");
        response.setToken("token-123");

        assertThat(response.getId()).isEqualTo(9L);
        assertThat(response.getFullName()).isEqualTo("Katherine Johnson");
        assertThat(response.getEmail()).isEqualTo("kj@example.com");
        assertThat(response.getPhoneNumber()).isEqualTo("123-456-789");
        assertThat(response.getToken()).isEqualTo("token-123");
    }

    @Test
    void allArgsConstructorPopulatesFields() {
        LoginResponse response = new LoginResponse(1L, "Tester", "t@example.com", "555-555-555", "token-value");

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getFullName()).isEqualTo("Tester");
        assertThat(response.getEmail()).isEqualTo("t@example.com");
        assertThat(response.getPhoneNumber()).isEqualTo("555-555-555");
        assertThat(response.getToken()).isEqualTo("token-value");
    }

    @Test
    void dataGeneratedMethodsCoverEqualityAndToString() {
        LoginResponse response = new LoginResponse(1L, "Tester", "t@example.com", "555-555-555", "token-value");
        LoginResponse sameResponse = new LoginResponse(1L, "Tester", "t@example.com", "555-555-555", "token-value");
        LoginResponse differentResponse = new LoginResponse(2L, "Tester", "t@example.com", "555-555-555", "token-value");

        assertThat(response)
                .isEqualTo(sameResponse)
                .hasSameHashCodeAs(sameResponse)
                .isNotEqualTo(differentResponse)
                .doesNotHaveSameHashCodeAs(differentResponse);

        assertThat(response.toString())
                .contains("Tester")
                .contains("t@example.com")
                .contains("555-555-555")
                .contains("token-value");
    }
}