package edu.uoc.epcsd.user.application.rest.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public final class CreateRentalRequest {

    @NotNull
    private final Long userId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
        private final LocalDate startDate;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate endDate;

    @NotNull
    private final List<RentalRequestProductRequest> products;
}
