package com.ansbeno.films.nationality;

import java.util.List;

import com.ansbeno.films.person.PersonDto;
import com.ansbeno.films.shared.base.BaseDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class NationalityDto extends BaseDto {

      private Long id;

      @NotBlank(message = "Nationality label is required")
      @Size(max = 100, message = "Nationality label must not exceed 100 characters")
      private String label;

      private List<PersonDto> persons;
}
