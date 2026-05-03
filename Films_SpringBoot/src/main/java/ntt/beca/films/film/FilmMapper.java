package ntt.beca.films.film;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ntt.beca.films.genre.GenreMapper;
import ntt.beca.films.nationality.NationalityMapper;
import ntt.beca.films.person.PersonMapper;
import ntt.beca.films.shared.base.BaseMapper;

@RequiredArgsConstructor
@Component
public class FilmMapper implements BaseMapper<Film, FilmDto> {

      private final GenreMapper genreMapper;
      private final NationalityMapper nationalityMapper;
      private final PersonMapper personMapper;

      public Film toEntity(FilmDto filmRequest) {
            return Film.builder()
                        .id(filmRequest.getId())
                        .title(filmRequest.getTitle())
                        .description(filmRequest.getDescription())
                        .year(filmRequest.getYear())
                        .duration(filmRequest.getDuration())
                        .genre(genreMapper.toEntity(filmRequest.getGenre()))
                        .nationality(nationalityMapper.toEntity(filmRequest.getNationality()))
                        .director(personMapper.toEntity(filmRequest.getDirector()))
                        .build();
      }

      public FilmDto toDto(Film film) {
            return FilmDto.builder()
                        .id(film.getId())
                        .title(film.getTitle())
                        .description(film.getDescription())
                        .year(film.getYear())
                        .duration(film.getDuration())
                        .genre(genreMapper.toDto(film.getGenre()))
                        .nationality(nationalityMapper.toDto(film.getNationality()))
                        .director(personMapper.toDto(film.getDirector()))
                        .build();
      }

}
