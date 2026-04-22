package ntt.beca.films.nationality;

import java.util.List;

import ntt.beca.films.person.PersonDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NationalityDto {

      private Long id;
      private String label;
      private List<PersonDto> persons;
}
