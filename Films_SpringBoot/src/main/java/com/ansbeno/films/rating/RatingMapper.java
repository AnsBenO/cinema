package com.ansbeno.films.rating;

import org.springframework.stereotype.Component;

import com.ansbeno.films.user.UserMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RatingMapper {

    private final UserMapper userMapper;

    public RatingDto toDto(FilmRating entity) {
        return RatingDto.builder()
                .id(entity.getId())
                .film(entity.getFilm().getTitle())
                .score(entity.getScore())
                .user(userMapper.toDto(entity.getCustomer()))
                .build();
    }
}
