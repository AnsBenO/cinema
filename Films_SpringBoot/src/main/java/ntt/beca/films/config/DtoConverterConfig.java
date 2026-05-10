package ntt.beca.films.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

import ntt.beca.films.film.FilmDto;
import ntt.beca.films.film.FilmService;
import ntt.beca.films.genre.GenreDto;
import ntt.beca.films.genre.GenreService;
import ntt.beca.films.hall.HallDto;
import ntt.beca.films.hall.HallService;
import ntt.beca.films.nationality.NationalityDto;
import ntt.beca.films.nationality.NationalityService;
import ntt.beca.films.person.PersonDto;
import ntt.beca.films.person.PersonService;

/**
 * Registers Spring {@link Converter} beans that allow MVC data binding to
 * convert a submitted form string (an entity ID) into the corresponding DTO.
 *
 * <p>
 * Concrete named inner classes are used instead of lambdas because Spring's
 * {@code ConversionService} resolves source/target types via reflection on the
 * generic type parameters of {@code Converter<S, T>}. Lambdas cause type
 * erasure, making it impossible for Spring to determine {@code <S>} and
 * {@code <T>} at runtime, which raises:
 * <em>"Unable to determine source type S and target type T"</em>.
 * </p>
 *
 * <p>
 * An empty or blank string is treated as {@code null} so that
 * {@code @NotNull} constraints on the DTO can report the "required" message
 * rather than a type-conversion error.
 * </p>
 */
@Configuration
class DtoConverterConfig {

      // -----------------------------------------------------------------------
      // GenreDto
      // -----------------------------------------------------------------------

      static final class StringToGenreDto implements Converter<String, GenreDto> {
            private final GenreService genreService;

            StringToGenreDto(GenreService genreService) {
                  this.genreService = genreService;
            }

            @Override
            public GenreDto convert(@NonNull String source) {
                  String trimmed = source.trim();
                  return trimmed.isEmpty() ? null : genreService.getOne(Long.parseLong(trimmed));
            }
      }

      @Bean
      StringToGenreDto stringToGenreDto(GenreService genreService) {
            return new StringToGenreDto(genreService);
      }

      // -----------------------------------------------------------------------
      // NationalityDto
      // -----------------------------------------------------------------------

      static final class StringToNationalityDto implements Converter<String, NationalityDto> {
            private final NationalityService nationalityService;

            StringToNationalityDto(NationalityService nationalityService) {
                  this.nationalityService = nationalityService;
            }

            @Override
            public NationalityDto convert(@NonNull String source) {
                  String trimmed = source.trim();
                  return trimmed.isEmpty() ? null : nationalityService.getOne(Long.parseLong(trimmed));
            }
      }

      @Bean
      StringToNationalityDto stringToNationalityDto(NationalityService nationalityService) {
            return new StringToNationalityDto(nationalityService);
      }

      // -----------------------------------------------------------------------
      // PersonDto (director + actors in FilmDto)
      // -----------------------------------------------------------------------

      static final class StringToPersonDto implements Converter<String, PersonDto> {
            private final PersonService personService;

            StringToPersonDto(PersonService personService) {
                  this.personService = personService;
            }

            @Override
            public PersonDto convert(@NonNull String source) {
                  String trimmed = source.trim();
                  return trimmed.isEmpty() ? null : personService.getOne(Long.parseLong(trimmed));
            }
      }

      @Bean
      StringToPersonDto stringToPersonDto(PersonService personService) {
            return new StringToPersonDto(personService);
      }

      // -----------------------------------------------------------------------
      // FilmDto (film in ScreeningDto)
      // -----------------------------------------------------------------------

      static final class StringToFilmDto implements Converter<String, FilmDto> {
            private final FilmService filmService;

            StringToFilmDto(FilmService filmService) {
                  this.filmService = filmService;
            }

            @Override
            public FilmDto convert(@NonNull String source) {
                  String trimmed = source.trim();
                  return trimmed.isEmpty() ? null : filmService.getOne(Long.parseLong(trimmed));
            }
      }

      @Bean
      StringToFilmDto stringToFilmDto(FilmService filmService) {
            return new StringToFilmDto(filmService);
      }

      // -----------------------------------------------------------------------
      // HallDto (hall in ScreeningDto)
      // -----------------------------------------------------------------------

      static final class StringToHallDto implements Converter<String, HallDto> {
            private final HallService hallService;

            StringToHallDto(HallService hallService) {
                  this.hallService = hallService;
            }

            @Override
            public HallDto convert(@NonNull String source) {
                  String trimmed = source.trim();
                  return trimmed.isEmpty() ? null : hallService.getOne(Long.parseLong(trimmed));
            }
      }

      @Bean
      StringToHallDto stringToHallDto(HallService hallService) {
            return new StringToHallDto(hallService);
      }
}
