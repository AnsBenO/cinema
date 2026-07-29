package com.ansbeno.films.booking;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateBookingPayload(
            @NotNull(message = "Screening id is required") @JsonProperty("screeningId") Long screeningId,

            @NotNull(message = "Quantity is required") @Min(value = 1, message = "Quantity must be at least 1") @JsonProperty("quantity") Integer quantity) {

}
