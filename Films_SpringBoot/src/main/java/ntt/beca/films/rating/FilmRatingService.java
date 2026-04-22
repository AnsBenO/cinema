package ntt.beca.films.rating;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ntt.beca.films.shared.service.CrudService;
import ntt.beca.films.shared.service.PagedResultDto;

@Service
public class FilmRatingService implements CrudService<FilmRating, Long> {

	private FilmRatingRepository filmRatingRepository;

	// constructor and repository injection
	public FilmRatingService(FilmRatingRepository filmRatingRepository) {
		this.filmRatingRepository = filmRatingRepository;
	}

	// saving method
	@Override
	public void save(FilmRating t) {
		filmRatingRepository.save(t);
	}

	// getting one film rating by Id
	@Override
	public FilmRating getOne(Long id) {
		return filmRatingRepository.findById(id).get();
	}

	// deleting one film rating by id
	@Override
	public boolean delete(Long id) {
		if (filmRatingRepository.existsById(id)) {
			filmRatingRepository.deleteById(id);
			return true;
		}
		return false;
	}

	// getting all the films ratings using pagination
	@Override
	public PagedResultDto<FilmRating> getAll(int pageNumber, String keyword, String genre) {
		Sort sort = Sort.by("id").ascending();
		pageNumber = pageNumber <= 1 ? 0 : pageNumber - 1;
		Pageable pageable = PageRequest.of(pageNumber, 5, sort);
		Page<FilmRating> filmRatingPage = genre.isEmpty() ? filmRatingRepository.findAll(pageable)
				: filmRatingRepository.findByFilmTitle(keyword, pageable);
		return PagedResultDto.<FilmRating>builder().data(filmRatingPage.toList())
				.totalElements(filmRatingPage.getTotalElements()).pageNumber(pageNumber + 1)
				.totalPages(filmRatingPage.getTotalPages()).isFirst(filmRatingPage.isFirst())
				.isLast(filmRatingPage.isLast()).hasNext(filmRatingPage.hasNext())
				.hasPrevious(filmRatingPage.hasPrevious()).build();

	}

}
