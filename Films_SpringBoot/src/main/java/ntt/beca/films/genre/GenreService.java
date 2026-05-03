package ntt.beca.films.genre;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ntt.beca.films.shared.exception.ResourceNotFoundException;
import ntt.beca.films.shared.service.CrudService;
import ntt.beca.films.shared.service.PagedResultDto;

@RequiredArgsConstructor
@Service
public class GenreService implements CrudService<GenreDto, Long> {

	private final GenreRepository genreRepository;

	private final GenreMapper genreMapper;

	// saving method
	@Override
	public void save(@NonNull GenreDto genreDto) {
		Genre genre = genreMapper.toEntity(genreDto);
		if (genre == null) {
			return;
		}
		genreRepository.save(genre);
	}

	// getting one genre by Id
	@Override
	public GenreDto getOne(@NonNull Long id) {
		Genre genre = genreRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Genre not found"));

		return genreMapper.toDto(genre);
	}

	// deleting one genre by Id
	@Override
	public boolean delete(@NonNull Long id) {
		if (genreRepository.existsById(id)) {
			genreRepository.deleteById(id);
			return true;
		}
		return false;
	}

	// getting all the genres using pagination
	@Override
	public PagedResultDto<GenreDto> getAll(int pageNumber, String keyword, String genre) {
		Sort sort = Sort.by("id").ascending();
		pageNumber = pageNumber <= 1 ? 0 : pageNumber - 1;
		Pageable pageable = PageRequest.of(pageNumber, 5, sort);
		Page<Genre> genrePage = genre.isEmpty() ? genreRepository.findAll(pageable)
				: genreRepository.findByLabelContaining(keyword, pageable);
		return PagedResultDto.<GenreDto>builder().data(genrePage.toList()
				.stream().map(genreMapper::toDto).toList())
				.totalElements(genrePage.getTotalElements()).pageNumber(pageNumber + 1)
				.totalPages(genrePage.getTotalPages()).isFirst(genrePage.isFirst())
				.isLast(genrePage.isLast()).hasNext(genrePage.hasNext())
				.hasPrevious(genrePage.hasPrevious()).build();

	}

	public List<GenreDto> getAllNoPagination() {
		List<Genre> genres = genreRepository.findAll();
		return genres.stream().map(genreMapper::toDto).toList();
	}

}
