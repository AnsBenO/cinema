package com.ansbeno.films.person;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ansbeno.films.film.Film;
import com.ansbeno.films.film.FilmRepository;
import com.ansbeno.films.shared.exception.ResourceNotFoundException;
import com.ansbeno.films.shared.service.CrudService;
import com.ansbeno.films.shared.service.PagedResultDto;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonService implements CrudService<PersonDto, Long> {

	private final PersonRepository personRepository;

	private final FilmRepository filmRepository;

	private final PersonMapper personMapper;

	// saving method
	@Override
	public void save(@NonNull PersonDto personDto) {
		Person person = personMapper.toEntity(personDto);
		if (person == null) {
			return;
		}
		personRepository.save(person);
	}

	// getting one person by Id
	@Override
	public PersonDto getOne(@NonNull Long id) {
		Person person = personRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Person not found"));

		return personMapper.toDto(person);
	}

	// deleting one person by Id
	@Override
	@Transactional
	public boolean delete(@NonNull Long personId) {
		// Fetch the person by ID
		Person person = personRepository.findById(personId)
				.orElseThrow(() -> new EntityNotFoundException("Person not found with ID: " + personId));

		if (person.getPersonType() == PersonType.ACTOR) {
			// Remove the person from the films' actors lists (update from the owning side)
			for (Film film : new ArrayList<>(person.getFilms())) {
				film.getActors().remove(person);
				filmRepository.save(film); // Save the owning entity to update the join table
			}
		} else {
			// Remove the person as a director from films
			for (Film film : new ArrayList<>(person.getDirectedFilms())) {
				film.setDirector(null); // Set director to null
				filmRepository.save(film); // Save the owning entity
			}
		}

		// Clear all associations from the person
		person.getFilms().clear();
		person.getDirectedFilms().clear();
		personRepository.save(person);

		// Delete the person
		personRepository.delete(person);

		// Return success status
		return !personRepository.existsById(personId);
	}

	@Override
	public PagedResultDto<PersonDto> getAll(int pageNumber, String keyword, String personType) {
		Sort sort = Sort.by("id").ascending();
		pageNumber = pageNumber <= 1 ? 0 : pageNumber - 1;
		Pageable pageable = PageRequest.of(pageNumber, 5, sort);

		PersonType typeFilter = personType.isEmpty() ? null
				: PersonType.valueOf(personType.toUpperCase());

		Page<Person> personPage = personRepository.findByKeywordAndPersonType(
				keyword.isEmpty() ? null : keyword,
				typeFilter,
				pageable);

		return PagedResultDto.<PersonDto>builder()
				.data(personPage.toList().stream().map(personMapper::toDto).toList())
				.totalElements(personPage.getTotalElements())
				.pageNumber(pageNumber + 1)
				.totalPages(personPage.getTotalPages())
				.isFirst(personPage.isFirst())
				.isLast(personPage.isLast())
				.hasNext(personPage.hasNext())
				.hasPrevious(personPage.hasPrevious())
				.build();
	}

	public List<PersonDto> getAllDirectorsNoPagination() {
		List<Person> person = personRepository.findByPersonType(PersonType.DIRECTOR);

		return person.stream().map(personMapper::toDto).toList();

	}

	public List<PersonDto> getAllActorsNoPagination() {
		List<Person> persons = personRepository.findByPersonType(PersonType.ACTOR);

		return persons.stream().map(personMapper::toDto).toList();
	}

}
