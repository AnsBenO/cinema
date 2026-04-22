package ntt.beca.films.person;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ntt.beca.films.film.Film;

import ntt.beca.films.film.FilmRepository;
import ntt.beca.films.shared.service.CrudService;
import ntt.beca.films.shared.service.PagedResultDto;

@Service
@RequiredArgsConstructor
public class PersonService implements CrudService<Person, Long> {

	private final PersonRepository personRepository;

	private final FilmRepository filmRepository;

	// saving method
	@Override
	public void save(Person t) {
		personRepository.save(t);
	}

	// getting one person by Id
	@Override
	public Person getOne(Long id) {
		return personRepository.findById(id).get();
	}

	// deleting one person by Id
	@Override
	@Transactional
	public boolean delete(Long personId) {
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
	public PagedResultDto<Person> getAll(int pageNumber, String keyword, String personType) {
		Sort sort = Sort.by("id").ascending();
		pageNumber = pageNumber <= 1 ? 0 : pageNumber - 1;
		Pageable pageable = PageRequest.of(pageNumber, 5, sort);

		PersonType typeFilter = personType.isEmpty() ? null
				: PersonType.valueOf(personType.toUpperCase());

		Page<Person> personPage = personRepository.findByKeywordAndPersonType(
				keyword.isEmpty() ? null : keyword,
				typeFilter,
				pageable);

		return PagedResultDto.<Person>builder()
				.data(personPage.toList())
				.totalElements(personPage.getTotalElements())
				.pageNumber(pageNumber + 1)
				.totalPages(personPage.getTotalPages())
				.isFirst(personPage.isFirst())
				.isLast(personPage.isLast())
				.hasNext(personPage.hasNext())
				.hasPrevious(personPage.hasPrevious())
				.build();
	}

	public List<Person> getAllDirectorsNoPagination() {
		return personRepository.findByPersonType(PersonType.DIRECTOR);
	}

	public List<Person> getAllActorsNoPagination() {
		return personRepository.findByPersonType(PersonType.ACTOR);
	}

}
