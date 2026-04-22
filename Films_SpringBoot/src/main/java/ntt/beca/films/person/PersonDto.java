package ntt.beca.films.person;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntt.beca.films.nationality.NationalityDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {
      private Long id;
      private String firstName;
      private String lastName;
      private String photo;
      @DateTimeFormat(pattern = "yyyy-MM-dd")
      private LocalDate birthDate;
      private PersonType personType;
      private NationalityDto nationality;
}
