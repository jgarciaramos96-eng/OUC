package edu.uoc.epcsd.user.application.rest.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor
public final class RentalRequestProductRequest {

    @NotNull
    private final Long productId;

    @NotNull
    @Positive
    private final Integer units;
}
