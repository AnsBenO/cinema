package ntt.beca.films.hall;

import org.springframework.stereotype.Component;

import ntt.beca.films.shared.base.BaseMapper;

@Component
public class HallMapper implements BaseMapper<Hall, HallDto> {

      @Override
      public Hall toEntity(HallDto dto) {
            return Hall.builder()
                        .id(dto.getId())
                        .number(dto.getNumber())
                        .capacity(dto.getCapacity()).build();
      }

      @Override
      public HallDto toDto(Hall entity) {
            return HallDto.builder()
                        .id(entity.getId())
                        .number(entity.getNumber())
                        .capacity(entity.getCapacity())
                        .build();
      }

}
