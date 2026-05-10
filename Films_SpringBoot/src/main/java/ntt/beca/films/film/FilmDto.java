package ntt.beca.films.film;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntt.beca.films.genre.GenreDto;
import ntt.beca.films.nationality.NationalityDto;
import ntt.beca.films.person.PersonDto;
import ntt.beca.films.shared.base.BaseDto;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilmDto extends BaseDto {
      private Long id;

      @NotBlank(message = "Title is required")
      @Size(max = 255, message = "Title must be at most 255 characters")
      private String title;

      @Positive(message = "Duration must be a positive number of minutes")
      private int duration;

      @Size(max = 2000, message = "Description must be at most 2000 characters")
      private String description;

      private String imageUrl;

      @Min(value = 1888, message = "Year must be 1888 or later")
      private int year;

      @NotNull(message = "Genre is required")
      private GenreDto genre;

      @NotNull(message = "Nationality is required")
      private NationalityDto nationality;

      @NotNull(message = "Director is required")
      private PersonDto director;

      private List<PersonDto> actors;
}
