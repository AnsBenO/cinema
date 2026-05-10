package ntt.beca.films.screening;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntt.beca.films.film.FilmDto;
import ntt.beca.films.hall.HallDto;
import ntt.beca.films.shared.base.BaseDto;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScreeningDto extends BaseDto {
      private Long id;

      @NotNull(message = "Start time is required")
      private LocalDateTime startTime;

      @NotNull(message = "End time is required")
      private LocalDateTime endTime;

      @NotNull(message = "Hall is required")
      private HallDto hall;

      @NotNull(message = "Film is required")
      private FilmDto film;
}
