package ntt.beca.films.rating;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntt.beca.films.user.UserDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingDto {
      private String film;
      private int score;
      private UserDto user;
}
