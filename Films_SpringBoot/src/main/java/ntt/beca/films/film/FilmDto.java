package ntt.beca.films.film;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntt.beca.films.genre.GenreDto;
import ntt.beca.films.nationality.NationalityDto;
import ntt.beca.films.person.PersonDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilmDto {
      private Long id;
      private String title;
      private int duration;
      private String description;
      private String imageUrl;
      private int year;
      private GenreDto genre;
      private NationalityDto nationality;
      private PersonDto director;
      private List<PersonDto> actors;
}
