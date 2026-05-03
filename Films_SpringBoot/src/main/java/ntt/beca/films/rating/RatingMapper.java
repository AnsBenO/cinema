package ntt.beca.films.rating;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ntt.beca.films.user.UserMapper;

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
