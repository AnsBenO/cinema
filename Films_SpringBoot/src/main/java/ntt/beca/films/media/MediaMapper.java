package ntt.beca.films.media;

import java.util.Objects;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ntt.beca.films.film.FilmRepository;
import ntt.beca.films.shared.base.BaseMapper;

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
