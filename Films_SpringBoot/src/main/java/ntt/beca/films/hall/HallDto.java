package ntt.beca.films.hall;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntt.beca.films.screening.ScreeningDto;
import ntt.beca.films.shared.base.BaseDto;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HallDto extends BaseDto {
      private Long id;

      @Positive(message = "Hall number must be a positive value")
      private int number;

      @Min(value = 1, message = "Capacity must be at least 1")
      private int capacity;

      @JsonIgnore
      private List<ScreeningDto> screenings;
}
