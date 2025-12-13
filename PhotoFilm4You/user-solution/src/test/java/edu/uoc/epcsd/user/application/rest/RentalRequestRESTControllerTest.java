package edu.uoc.epcsd.user.application.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.uoc.epcsd.user.domain.RentalRequest;
import edu.uoc.epcsd.user.domain.RentalRequestProduct;
import edu.uoc.epcsd.user.domain.service.RentalRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RentalRequestRESTControllerTest {

    @Mock
    private RentalRequestService rentalRequestService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        RentalRequestRESTController controller = new RentalRequestRESTController(rentalRequestService);
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    private RentalRequest buildRentalRequest(Long id) {
        return RentalRequest.builder()
                .id(id)
                .userId(10L)
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 1, 4))
                .products(List.of(
                        RentalRequestProduct.builder()
                                .productId(100L)
                                .units(2)
                                .build()
                ))
                .build();
    }


    @Test
    void createRentalRequestReturns201() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new com.fasterxml.jackson.module.paramnames.ParameterNamesModule());

        String jsonRequest = "{"
                + "\"userId\":10,"
                + "\"startDate\":\"2025-01-01\","
                + "\"endDate\":\"2025-01-04\","
                + "\"products\":[{\"productId\":100,\"units\":2}]"
                + "}";

        when(rentalRequestService.createRentalRequest(any(RentalRequest.class))).thenReturn(1L);

        mockMvc.perform(post("/rentalRequests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/rentalRequests/1"))
                .andExpect(content().string("1"));
    }

    @Test
    void createRentalRequestUserNotFoundReturns400() throws Exception {
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.registerModule(new com.fasterxml.jackson.module.paramnames.ParameterNamesModule());

        String jsonRequest = "{"
                + "\"userId\":10,"
                + "\"startDate\":\"2025-01-01\","
                + "\"endDate\":\"2025-01-04\","
                + "\"products\":[{\"productId\":100,\"units\":2}]"
                + "}";

        when(rentalRequestService.createRentalRequest(any(RentalRequest.class)))
                .thenThrow(new IllegalArgumentException("User not found"));

        mockMvc.perform(post("/rentalRequests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }


    @Test
    void createRentalRequestProductNotFoundReturns400() throws Exception {
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.registerModule(new com.fasterxml.jackson.module.paramnames.ParameterNamesModule());

        String jsonRequest = "{"
                + "\"userId\":10,"
                + "\"startDate\":\"2025-01-01\","
                + "\"endDate\":\"2025-01-04\","
                + "\"products\":[{\"productId\":100,\"units\":2}]"
                + "}";

        when(rentalRequestService.createRentalRequest(any(RentalRequest.class)))
                .thenThrow(new RestClientException("Product not found"));

        mockMvc.perform(post("/rentalRequests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }


    @Test
    void getAllRentalRequestsReturns200() throws Exception {
        when(rentalRequestService.findAllRentalRequests())
                .thenReturn(List.of(buildRentalRequest(1L)));

        mockMvc.perform(get("/rentalRequests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userId").value(10L));
    }

    @Test
    void getRentalRequestByIdReturns200() throws Exception {
        when(rentalRequestService.findRentalRequestById(1L))
                .thenReturn(Optional.of(buildRentalRequest(1L)));

        mockMvc.perform(get("/rentalRequests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(10L));
    }

    @Test
    void getRentalRequestByIdReturns404() throws Exception {
        when(rentalRequestService.findRentalRequestById(1L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/rentalRequests/1"))
                .andExpect(status().isNotFound());
    }
}
