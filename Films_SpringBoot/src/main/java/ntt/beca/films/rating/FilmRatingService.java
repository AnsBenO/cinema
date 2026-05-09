package ntt.beca.films.rating;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ntt.beca.films.shared.exception.ResourceNotFoundException;
import ntt.beca.films.shared.service.PagedResultDto;

@RequiredArgsConstructor
@Service
public class FilmRatingService {

	private final FilmRatingRepository filmRatingRepository;
	private final RatingMapper ratingMapper;

	public RatingDto getOne(@NonNull Long id) {
		FilmRating rating = filmRatingRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Rating not found"));

		return ratingMapper.toDto(rating);
	}

	// deleting one film rating by id

	public boolean delete(@NonNull Long id) {
		if (filmRatingRepository.existsById(id)) {
			filmRatingRepository.deleteById(id);
			return true;
		}
		return false;
	}

	// getting all the films ratings using pagination

	public PagedResultDto<RatingDto> getAll(int pageNumber, String keyword) {
		Sort sort = Sort.by("id").ascending();
		pageNumber = pageNumber <= 1 ? 0 : pageNumber - 1;
		Pageable pageable = PageRequest.of(pageNumber, 5, sort);
		Page<FilmRating> filmRatingPage = keyword.isEmpty() ? filmRatingRepository.findAll(pageable)
				: filmRatingRepository.findByFilmTitle(keyword, pageable);
		return PagedResultDto.<RatingDto>builder().data(filmRatingPage.toList()
				.stream().map(ratingMapper::toDto).toList())
				.totalElements(filmRatingPage.getTotalElements()).pageNumber(pageNumber + 1)
				.totalPages(filmRatingPage.getTotalPages()).isFirst(filmRatingPage.isFirst())
				.isLast(filmRatingPage.isLast()).hasNext(filmRatingPage.hasNext())
				.hasPrevious(filmRatingPage.hasPrevious()).build();

	}

}
