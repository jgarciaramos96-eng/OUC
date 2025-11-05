package edu.uoc.epcsd.digital.domain;

import lombok.*;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DigitalItem {
	
    @NotNull
    private Long id;
    
    @NotNull
    private Long digitalsessionid;

    @NotNull
    private String description;
    
	@NotNull
    private Long lat;
    
    @NotNull
    private Long lon;
    
    @NotNull
    private String link;

    @NotNull
    @Builder.Default
    private DigitalStatus status = DigitalStatus.AVAILABLE;

}
