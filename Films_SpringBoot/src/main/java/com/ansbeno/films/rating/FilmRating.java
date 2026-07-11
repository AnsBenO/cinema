package com.ansbeno.films.rating;

import com.ansbeno.films.film.Film;
import com.ansbeno.films.shared.base.AbstractEntity;
import com.ansbeno.films.user.UserEntity;

import jakarta.persistence.Entity;
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
@Table(name = "film_rating")
public class FilmRating extends AbstractEntity {

      @ManyToOne
      @JoinColumn(name = "customer_id", nullable = false)
      private UserEntity customer;

      @ManyToOne
      @JoinColumn(name = "film_id", nullable = false)
      private Film film;

      private int score;

}
