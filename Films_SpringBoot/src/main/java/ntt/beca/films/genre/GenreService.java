package ntt.beca.films.genre;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ntt.beca.films.shared.service.CrudService;
import ntt.beca.films.shared.service.PagedResultDto;

@Service
public class GenreService implements CrudService<Genre, Long> {

	private GenreRepository genreRepository;

	// constructor and repository injection
	public GenreService(GenreRepository genreRepository) {
		this.genreRepository = genreRepository;
	}

	// saving method
	@Override
	public void save(Genre t) {
		genreRepository.save(t);
	}

	// getting one genre by Id
	@Override
	public Genre getOne(Long id) {
		return genreRepository.findById(id).get();
	}

	// deleting one genre by Id
	@Override
	public boolean delete(Long id) {
		if (genreRepository.existsById(id)) {
			genreRepository.deleteById(id);
			return true;
		}
		return false;
	}

	// getting all the genres using pagination
	@Override
	public PagedResultDto<Genre> getAll(int pageNumber, String keyword, String genre) {
		Sort sort = Sort.by("id").ascending();
		pageNumber = pageNumber <= 1 ? 0 : pageNumber - 1;
		Pageable pageable = PageRequest.of(pageNumber, 5, sort);
		Page<Genre> genrePage = genre.isEmpty() ? genreRepository.findAll(pageable)
				: genreRepository.findByLabelContaining(keyword, pageable);
		return PagedResultDto.<Genre>builder().data(genrePage.toList())
				.totalElements(genrePage.getTotalElements()).pageNumber(pageNumber + 1)
				.totalPages(genrePage.getTotalPages()).isFirst(genrePage.isFirst())
				.isLast(genrePage.isLast()).hasNext(genrePage.hasNext())
				.hasPrevious(genrePage.hasPrevious()).build();

	}

	public List<Genre> getAllNoPagination() {
		return genreRepository.findAll();
	}

}
