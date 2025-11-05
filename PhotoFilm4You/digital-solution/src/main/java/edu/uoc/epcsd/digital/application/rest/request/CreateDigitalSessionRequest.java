package edu.uoc.epcsd.digital.application.rest.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public final class CreateDigitalSessionRequest {
	   
	@NotBlank
    private final String email;

	@NotBlank
    private final String description;
	
}
