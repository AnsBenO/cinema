package com.ansbeno.films.rating;

import com.ansbeno.films.shared.base.BaseDto;
import com.ansbeno.films.user.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
