package ntt.beca.films.rating;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RatingPayload(
            @JsonProperty("film") String film,
            @JsonProperty("score") Integer score) {

}
