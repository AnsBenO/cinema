package com.ansbeno.films.person;

import org.springframework.stereotype.Component;

import com.ansbeno.films.nationality.Nationality;
import com.ansbeno.films.nationality.NationalityMapper;
import com.ansbeno.films.nationality.NationalityRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PersonMapper {

      private final NationalityMapper nationalityMapper;

      private final NationalityRepository nationalityRepository;

      public PersonDto toDto(Person person) {
            PersonDto personDto = new PersonDto();
            personDto.setId(person.getId());
            personDto.setFirstName(person.getFirstName());
            personDto.setLastName(person.getLastName());
            personDto.setPhoto(person.getPhoto());
            personDto.setBirthDate(person.getBirthDate());
            personDto.setPersonType(person.getPersonType());
            Long nationalityId = person.getNationality().getId();
            if (nationalityId == null) {
                  return personDto;
            }
            Nationality nationality = nationalityRepository
                        .findById(nationalityId)
                        .orElseThrow(() -> new RuntimeException("Nationality not found"));

            personDto.setNationality(nationalityMapper.toDto(nationality));
            personDto.setCreatedAt(person.getCreatedAt());
            personDto.setUpdatedAt(person.getUpdatedAt());
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
            Long nationalityId = personDto.getNationality().getId();
            if (nationalityId == null) {
                  return person;
            }
            Nationality nationality = nationalityRepository
                        .findById(nationalityId)
                        .orElseThrow(() -> new RuntimeException("Nationality not found"));
            person.setNationality(nationality);
            person.setCreatedAt(personDto.getCreatedAt());
            person.setUpdatedAt(personDto.getUpdatedAt());
            return person;
      }

}
