package edu.uoc.epcsd.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ApplicationConfigurationTest {

    private ApplicationConfiguration configuration;

    @BeforeEach
    void setUp() {
        configuration = new ApplicationConfiguration();
    }

    @Test
    void restTemplateBeanIsCreatedFromBuilder() {
        RestTemplateBuilder builder = new RestTemplateBuilder();

        RestTemplate template = configuration.restTemplate(builder);

        assertThat(template).isNotNull();
    }

    @Test
    void corsMappingsAreConfiguredForFrontendOrigins() {
        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration registration = mock(CorsRegistration.class);
        when(registry.addMapping("/**")).thenReturn(registration);
        when(registration.allowedOrigins(anyString(), anyString(), anyString())).thenReturn(registration);
        when(registration.allowedMethods(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(registration);
        when(registration.allowedHeaders("*")).thenReturn(registration);
        when(registration.allowCredentials(anyBoolean())).thenReturn(registration);

        configuration.addCorsMappings(registry);

        verify(registry).addMapping("/**");
        verify(registration).allowedOrigins("http://localhost", "http://localhost:80", "http://localhost:5173");
        verify(registration).allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
        verify(registration).allowedHeaders("*");
        verify(registration).allowCredentials(true);
    }
}
