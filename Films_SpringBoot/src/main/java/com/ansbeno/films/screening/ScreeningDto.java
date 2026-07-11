package com.ansbeno.films.screening;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.ansbeno.films.film.FilmDto;
import com.ansbeno.films.hall.HallDto;
import com.ansbeno.films.shared.base.BaseDto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScreeningDto extends BaseDto {
      private Long id;

      @NotNull(message = "Start time is required")
      @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
      private LocalDateTime startTime;

      @NotNull(message = "End time is required")
      @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
      private LocalDateTime endTime;

      @NotNull(message = "Hall is required")
      private HallDto hall;

      @NotNull(message = "Film is required")
      private FilmDto film;
}
