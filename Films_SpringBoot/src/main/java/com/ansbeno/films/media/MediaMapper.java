package com.ansbeno.films.media;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.ansbeno.films.film.FilmRepository;
import com.ansbeno.films.shared.base.BaseMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MediaMapper implements BaseMapper<Media, MediaDto> {

      private final FilmRepository filmRepository;

      @Override
      public Media toEntity(MediaDto dto) {
            Long filmId = Objects.requireNonNull(dto.getFilmId(), "filmId must not be null");

            return Media.builder()
                        .id(dto.getId())
                        .media(dto.getMedia())
                        .mediaType(dto.getMediaType())
                        .film(filmRepository.findById(filmId).orElseThrow(
                                    () -> new IllegalArgumentException("Film not found for media: " + dto.getId())))
                        .build();
      }

      @Override
      public MediaDto toDto(Media entity) {
            return MediaDto.builder()
                        .id(entity.getId())
                        .media(entity.getMedia())
                        .mediaType(entity.getMediaType())
                        .filmId(entity.getFilm().getId())
                        .build();
      }

}
