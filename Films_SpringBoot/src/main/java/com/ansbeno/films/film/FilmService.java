package com.ansbeno.films.film;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ansbeno.films.shared.exception.ResourceNotFoundException;
import com.ansbeno.films.shared.service.CrudService;
import com.ansbeno.films.shared.service.PagedResultDto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
@Service
public class FilmService implements CrudService<FilmDto, Long> {

	private final FilmMapper filmMapper;
	private final FilmRepository filmRepository;

	// saving method
	@Override
	public void save(@NonNull FilmDto filmDto) {
		Film film = filmMapper.toEntity(filmDto);
		if (film == null) {
			return;
		}
		filmRepository.save(film);
	}

	// getting one film by Id
	@Override
	public FilmDto getOne(@NonNull Long id) {
		Film film = filmRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Film not found"));

		return filmMapper.toDto(film);
	}

	// deleting one film by Id
	@Override
	public boolean delete(@NonNull Long id) {
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
	public PagedResultDto<FilmDto> getAll(int pageNumber, String keyword, String genre) {
		Sort sort = Sort.by("id").ascending();
		pageNumber = pageNumber <= 1 ? 0 : pageNumber - 1;
		Pageable pageable = PageRequest.of(pageNumber, 5, sort);

		// Normalize inputs for NULL values
		String normalizedKeyword = keyword.isBlank() ? null : keyword;
		String normalizedGenre = genre.isBlank() ? null : genre;

		Page<Film> filmPage = filmRepository.findByTitleAndGenre(normalizedKeyword, normalizedGenre, pageable);

		return PagedResultDto.<FilmDto>builder()
				.data(filmPage.toList().stream().map(filmMapper::toDto).toList())
				.totalElements(filmPage.getTotalElements())
				.pageNumber(pageNumber + 1)
				.totalPages(filmPage.getTotalPages())
				.isFirst(filmPage.isFirst())
				.isLast(filmPage.isLast())
				.hasNext(filmPage.hasNext())
				.hasPrevious(filmPage.hasPrevious())
				.build();
	}

	public List<FilmDto> getAllNoPagination() {
		List<Film> films = filmRepository.findAll();
		return films.stream().map(filmMapper::toDto).toList();
	}
}
