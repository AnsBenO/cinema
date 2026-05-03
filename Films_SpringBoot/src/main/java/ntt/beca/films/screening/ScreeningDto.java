package ntt.beca.films.screening;

import java.time.LocalDateTime;

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
      private LocalDateTime startTime;
      private LocalDateTime endTime;
      private HallDto hall;
      private FilmDto film;
}
