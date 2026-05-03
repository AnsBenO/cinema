package ntt.beca.films.hall;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
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
      @NotNull
      private Long id;
      @NotNull
      private int number;
      @NotNull
      private int capacity;

      @JsonIgnore
      private List<ScreeningDto> screenings;
}
