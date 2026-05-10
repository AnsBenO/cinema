package ntt.beca.films.rating;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RatingPayload(
            @NotBlank(message = "Film title is required") @JsonProperty("film") String film,

            @NotNull(message = "Score is required") @Min(value = 1, message = "Score must be at least 1") @Max(value = 10, message = "Score must be at most 10") @JsonProperty("score") Integer score) {

}
