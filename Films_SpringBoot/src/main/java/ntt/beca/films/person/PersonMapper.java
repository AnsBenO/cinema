package ntt.beca.films.person;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ntt.beca.films.nationality.NationalityMapper;

@Component
@RequiredArgsConstructor
public class PersonMapper {

      private final NationalityMapper nationalityMapper;

      public PersonDto toDto(Person person) {
            PersonDto personDto = new PersonDto();
            personDto.setId(person.getId());
            personDto.setFirstName(person.getFirstName());
            personDto.setLastName(person.getLastName());
            personDto.setPhoto(person.getPhoto());
            personDto.setBirthDate(person.getBirthDate());
            personDto.setPersonType(person.getPersonType());
            personDto.setNationality(nationalityMapper.toDto(person.getNationality()));
            return personDto;
      }

      public Person toEntity(PersonDto personDto) {
            Person person = new Person();
            person.setId(personDto.getId());
            person.setFirstName(personDto.getFirstName());
            person.setLastName(personDto.getLastName());
            person.setPhoto(personDto.getPhoto());
            person.setBirthDate(personDto.getBirthDate());
            person.setPersonType(personDto.getPersonType());
            person.setNationality(nationalityMapper.toEntity(personDto.getNationality()));
            return person;
      }

}
