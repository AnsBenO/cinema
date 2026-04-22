package ntt.beca.films.rating;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntt.beca.films.shared.base.AbstractEntity;
import ntt.beca.films.user.UserEntity;
import ntt.beca.films.film.Film;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "film_rating")
public class FilmRating extends AbstractEntity {

      @ManyToOne
      @JoinColumn(name = "customer_id",nullable = false)
      private UserEntity customer;

      @ManyToOne
      @JoinColumn(name = "film_id",nullable = false)
      private Film film;

      private int score;

}
