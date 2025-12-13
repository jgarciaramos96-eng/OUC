package edu.uoc.epcsd.user.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RentalRequestProduct {

    @NotNull
    private Long productId;

    @NotNull
    @Positive
    private Integer units;

}
