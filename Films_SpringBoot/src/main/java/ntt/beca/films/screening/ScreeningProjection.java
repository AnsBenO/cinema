package ntt.beca.films.screening;

import java.time.LocalDateTime;

import org.springframework.data.rest.core.config.Projection;

import ntt.beca.films.film.Film;
import ntt.beca.films.hall.Hall;

@Projection(name = "inlineDetails", types = Screening.class)
public interface ScreeningProjection {
      Long getId();

      LocalDateTime getStartTime();

      LocalDateTime getEndTime();

      Film getFilm();

      Hall getHall();
}
