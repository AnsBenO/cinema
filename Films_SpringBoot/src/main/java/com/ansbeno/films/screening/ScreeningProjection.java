package com.ansbeno.films.screening;

import java.time.LocalDateTime;

import org.springframework.data.rest.core.config.Projection;

import com.ansbeno.films.film.Film;
import com.ansbeno.films.hall.Hall;

@Projection(name = "inlineDetails", types = Screening.class)
public interface ScreeningProjection {
      Long getId();

      LocalDateTime getStartTime();

      LocalDateTime getEndTime();

      Film getFilm();

      Hall getHall();
}
