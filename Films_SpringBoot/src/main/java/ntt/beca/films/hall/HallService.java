package ntt.beca.films.hall;

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
public class HallService implements CrudService<HallDto, Long> {

	private final HallRepository hallRepository;
	private final HallMapper hallMapper;

	// saving method
	@Override
	public void save(@NonNull HallDto hallDto) {
		Hall hall = hallMapper.toEntity(hallDto);
		if (hall == null) {
			return;
		}
		hallRepository.save(hall);
	}

	// getting one hall by Id
	@Override
	public HallDto getOne(@NonNull Long id) {
		Hall hall = hallRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hall not found"));

		return hallMapper.toDto(hall);

	}

	// deleting one hall by Id
	@Override
	public boolean delete(@NonNull Long id) {
		if (hallRepository.existsById(id)) {
			hallRepository.deleteById(id);
			return true;
		}
		return false;
	}

	// getting all the hall using pagination
	@Override
	public PagedResultDto<HallDto> getAll(int pageNumber, String keyword, String screening) {
		return null;
	}

	public PagedResultDto<HallDto> getAll(int pageNumber, String keyword) {
		Sort sort = Sort.by("id").ascending();
		pageNumber = pageNumber <= 1 ? 0 : pageNumber - 1;
		Pageable pageable = PageRequest.of(pageNumber, 5, sort);
		Page<Hall> hallPage = hallRepository.findByCapacity(keyword, pageable);
		return PagedResultDto.<HallDto>builder().data(hallPage.toList().stream()
				.map(hallMapper::toDto).toList())
				.totalElements(hallPage.getTotalElements()).pageNumber(pageNumber + 1)
				.totalPages(hallPage.getTotalPages()).isFirst(hallPage.isFirst())
				.isLast(hallPage.isLast()).hasNext(hallPage.hasNext())
				.hasPrevious(hallPage.hasPrevious()).build();

	}

	public List<HallDto> getAllNoPagination() {
		List<Hall> halls = hallRepository.findAll();
		return halls.stream().map(hallMapper::toDto).toList();
	}

}
