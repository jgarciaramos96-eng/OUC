package edu.uoc.epcsd.digital.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DigitalSession {

    @NotNull
    private Long id;

    @NotNull
    private String email;
    @NotNull

    private String description;

    @NotNull
    @Builder.Default
    private DigitalStatus status = DigitalStatus.AVAILABLE;

}
