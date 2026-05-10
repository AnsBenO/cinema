package ntt.beca.films.nationality;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ntt.beca.films.person.PersonDto;
import ntt.beca.films.shared.base.BaseDto;
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
