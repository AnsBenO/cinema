package ntt.beca.films.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntt.beca.films.shared.base.BaseDto;
import ntt.beca.films.user.UserDto;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingDto extends BaseDto {
      private Long id;
      private String film;
      private int score;
      private UserDto user;
}
