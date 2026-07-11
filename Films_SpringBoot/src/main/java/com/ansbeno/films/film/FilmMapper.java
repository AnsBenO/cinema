package com.ansbeno.films.film;

import org.springframework.stereotype.Component;

import com.ansbeno.films.genre.GenreMapper;
import com.ansbeno.films.nationality.NationalityMapper;
import com.ansbeno.films.person.PersonMapper;
import com.ansbeno.films.shared.base.BaseMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FilmMapper implements BaseMapper<Film, FilmDto> {

      private final GenreMapper genreMapper;
      private final NationalityMapper nationalityMapper;
      private final PersonMapper personMapper;

      public Film toEntity(FilmDto dto) {
            return Film.builder()
                        .id(dto.getId())
                        .title(dto.getTitle())
                        .description(dto.getDescription())
                        .imageUrl(dto.getImageUrl())
                        .year(dto.getYear())
                        .duration(dto.getDuration())
                        .genre(genreMapper.toEntity(dto.getGenre()))
                        .nationality(nationalityMapper.toEntity(dto.getNationality()))
                        .director(personMapper.toEntity(dto.getDirector()))
                        .build();
      }

      public FilmDto toDto(Film film) {
            return FilmDto.builder()
                        .id(film.getId())
                        .title(film.getTitle())
                        .description(film.getDescription())
                        .imageUrl(film.getImageUrl())
                        .year(film.getYear())
                        .duration(film.getDuration())
                        .genre(genreMapper.toDto(film.getGenre()))
                        .nationality(nationalityMapper.toDto(film.getNationality()))
                        .director(personMapper.toDto(film.getDirector()))
                        .build();
      }

}
