package com.ansbeno.films.screening;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.ansbeno.films.film.Film;
import com.ansbeno.films.hall.Hall;
import com.ansbeno.films.shared.base.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "screenings")
public class Screening extends AbstractEntity {

      @Column(name = "start_time")
      @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
      private LocalDateTime startTime;

      @Column(name = "end_time")
      @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
      private LocalDateTime endTime;

      @ManyToOne(fetch = FetchType.EAGER)
      @JoinColumn(name = "film_id", nullable = false)
      private Film film;

      @ManyToOne(fetch = FetchType.EAGER)
      @JoinColumn(name = "hall_id", nullable = false)

      private Hall hall;

}
