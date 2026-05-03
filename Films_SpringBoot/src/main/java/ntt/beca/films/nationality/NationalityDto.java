package ntt.beca.films.nationality;

import java.util.List;

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
      private String label;
      private List<PersonDto> persons;
}
