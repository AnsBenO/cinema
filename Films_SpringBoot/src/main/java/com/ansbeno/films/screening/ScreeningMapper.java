package com.ansbeno.films.screening;

import org.springframework.stereotype.Component;

import com.ansbeno.films.film.FilmMapper;
import com.ansbeno.films.hall.HallMapper;
import com.ansbeno.films.shared.base.BaseMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ScreeningMapper implements BaseMapper<Screening, ScreeningDto> {

      private final HallMapper hallMapper;
      private final FilmMapper filmMapper;

      @Override
      public ScreeningDto toDto(Screening entity) {
            return ScreeningDto.builder()
                        .id(entity.getId())
                        .startTime(entity.getStartTime())
                        .endTime(entity.getEndTime())
                        .hall(hallMapper.toDto(entity.getHall()))
                        .film(filmMapper.toDto(entity.getFilm()))
                        .build();
      }

      @Override
      public Screening toEntity(ScreeningDto dto) {
            return Screening.builder()
                        .id(dto.getId())
                        .startTime(dto.getStartTime())
                        .endTime(dto.getEndTime())
                        .hall(hallMapper.toEntity(dto.getHall()))
                        .film(filmMapper.toEntity(dto.getFilm()))
                        .build();
      }

}
