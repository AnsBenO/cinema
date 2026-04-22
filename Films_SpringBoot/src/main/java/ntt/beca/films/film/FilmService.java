package ntt.beca.films.film;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import ntt.beca.films.shared.service.CrudService;
import ntt.beca.films.shared.service.PagedResultDto;

@Service
public class FilmService implements CrudService<Film, Long> {

	private FilmRepository filmRepository;

	// constructor and repository injection
	public FilmService(FilmRepository filmRepository) {
		this.filmRepository = filmRepository;
	}

	// saving method
	@Override
	public void save(Film film) {
		filmRepository.save(film);
	}

	// getting one film by Id
	@Override
	public Film getOne(Long id) {
		return filmRepository.findById(id).get();
	}

	// deleting one film by Id
	@Override
	public boolean delete(Long id) {
		if (filmRepository.existsById(id)) {
			filmRepository.deleteById(id);
			return true;
		}
		return false;
	}

	public List<Film> getAll() {
		return filmRepository.findAll();
	}

	@Override
	public PagedResultDto<Film> getAll(int pageNumber, String keyword, String genre) {
		Sort sort = Sort.by("id").ascending();
		pageNumber = pageNumber <= 1 ? 0 : pageNumber - 1;
		Pageable pageable = PageRequest.of(pageNumber, 5, sort);

		// Normalize inputs for NULL values
		String normalizedKeyword = keyword.isBlank() ? null : keyword;
		String normalizedGenre = genre.isBlank() ? null : genre;

		Page<Film> filmPage = filmRepository.findByTitleAndGenre(normalizedKeyword, normalizedGenre, pageable);

		return PagedResultDto.<Film>builder()
				.data(filmPage.toList())
				.totalElements(filmPage.getTotalElements())
				.pageNumber(pageNumber + 1)
				.totalPages(filmPage.getTotalPages())
				.isFirst(filmPage.isFirst())
				.isLast(filmPage.isLast())
				.hasNext(filmPage.hasNext())
				.hasPrevious(filmPage.hasPrevious())
				.build();
	}

	public List<Film> getAllNoPagination() {
		return filmRepository.findAll();
	}
}
