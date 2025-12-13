package edu.uoc.epcsd.productcatalog.application.rest.request;

import lombok.*;
import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
public final class UpdateProductRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull @Min(0)
    private Double dailyPrice;

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotNull
    private Long categoryId;
}
