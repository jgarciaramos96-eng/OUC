package edu.uoc.epcsd.user.application.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uoc.epcsd.user.application.rest.request.LoginRequest;
import edu.uoc.epcsd.user.domain.User;
import edu.uoc.epcsd.user.domain.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserRESTControllerTest {

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private User buildUser() {
        return User.builder().id(42L).fullName("Ada Lovelace").email("ada@example.com").password("pass").phoneNumber("999-888-777").build();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        UserRESTController controller = new UserRESTController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void loginReturnsUserAuthenticationSucceeds() throws Exception {
        User user = buildUser();
        when(userService.authenticate(user.getEmail(), user.getPassword())).thenReturn(Optional.of(user));

        LoginRequest request = new LoginRequest();
        request.setEmail(user.getEmail());
        request.setPassword(user.getPassword());

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"id\":" + user.getId() + "," +
                        "\"fullName\":\"" + user.getFullName() + "\"," +
                        "\"email\":\"" + user.getEmail() + "\"," +
                        "\"phoneNumber\":\"" + user.getPhoneNumber() + "\"" +
                        "}"));
    }

    @Test
    void loginUnauthorizedAuthenticationFails() throws Exception {
        when(userService.authenticate(anyString(), anyString())).thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest();
        request.setEmail("aaaaaaaaa@example.com");
        request.setPassword("AaAAAaAA");

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

}