package ntt.beca.films.genre;

import org.springframework.stereotype.Component;

import ntt.beca.films.shared.base.BaseMapper;

@Component
public class GenreMapper implements BaseMapper<Genre, GenreDto> {
      @Override
      public Genre toEntity(GenreDto dto) {
            return Genre.builder()
                        .id(dto.getId())
                        .label(dto.getLabel()).build();
      }

      @Override
      public GenreDto toDto(Genre entity) {
            return GenreDto.builder()
                        .id(entity.getId())
                        .label(entity.getLabel()).build();
      }

}
