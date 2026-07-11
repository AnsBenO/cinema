package com.ansbeno.films.shared.base;

public interface BaseMapper<E, D> {
      E toEntity(D dto);

      D toDto(E entity);

}
