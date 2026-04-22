package ntt.beca.films.film;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntt.beca.films.shared.base.AbstractEntity;
import ntt.beca.films.genre.Genre;
import ntt.beca.films.nationality.Nationality;
import ntt.beca.films.person.Person;
import ntt.beca.films.rating.FilmRating;
import ntt.beca.films.screening.Screening;
import ntt.beca.films.media.Media;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "films")
public class Film extends AbstractEntity {
      @Column(nullable = false, length = 50)
      private String title;

      @Column(nullable = false)
      private int duration;

      private String description;

      private String imageUrl;

      @Column(nullable = false, name = "release_year")
      private int year;

      @ManyToOne(fetch = FetchType.EAGER)
      @JoinColumn(name = "genre_id", nullable = false)
      private Genre genre;

      @ManyToOne(fetch = FetchType.EAGER)
      @JoinColumn(name = "nationality_id", nullable = false)
      @JsonProperty
      private Nationality nationality;

      @ManyToOne(fetch = FetchType.EAGER)
      @JoinColumn(name = "director_id", nullable = true)
      private Person director;

      @ManyToMany(fetch = FetchType.EAGER)
      private List<Person> actors;

      @OneToMany(mappedBy = "film", cascade = CascadeType.REMOVE)
      @JsonIgnore
      private List<Screening> screenings;

      @OneToMany(mappedBy = "film", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
      @JsonIgnore
      private List<Media> medias;

      @OneToMany(mappedBy = "film", cascade = CascadeType.ALL)
      private List<FilmRating> ratings;

}
