package ntt.beca.films.nationality;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ntt.beca.films.shared.exception.ResourceNotFoundException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ntt.beca.films.shared.service.CrudService;
import ntt.beca.films.shared.service.PagedResultDto;

@RequiredArgsConstructor
@Service
public class NationalityService implements CrudService<NationalityDto, Long> {

	private final NationalityRepository nationalityRepository;
	private final NationalityMapper nationalityMapper;

	// saving method
	@Override
	@Transactional
	public void save(@NonNull NationalityDto nationalityDto) {
		Long nationalityId = nationalityDto.getId();
		if (nationalityId != null) {
			Nationality existing = nationalityRepository.findById(nationalityId)
					.orElseThrow(() -> new ResourceNotFoundException("Nationality not found"));
			existing.setLabel(nationalityDto.getLabel());
			// No explicit save() needed — entity is managed within @Transactional context;
			// Hibernate auto-flushes dirty state at transaction commit.
		} else {
			Nationality nationality = nationalityMapper.toEntity(nationalityDto);
			if (nationality == null) {
				return;
			}
			nationalityRepository.save(nationality);
		}
	}

	// getting one nationality by Id
	@Override
	public NationalityDto getOne(@NonNull Long id) {
		Nationality nationality = nationalityRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Nationality not found"));

		return nationalityMapper.toDto(nationality);
	}

	// deleting one nationality by Id
	@Override
	public boolean delete(@NonNull Long id) {
		if (nationalityRepository.existsById(id)) {
			nationalityRepository.deleteById(id);
			return true;
		}
		return false;
	}

	// getting all the nationalities using pagination
	@Override
	public PagedResultDto<NationalityDto> getAll(int pageNumber, String keyword, String genre) {
		Sort sort = Sort.by("id").ascending();
		pageNumber = pageNumber <= 1 ? 0 : pageNumber - 1;
		Pageable pageable = PageRequest.of(pageNumber, 5, sort);
		Page<Nationality> nationalityPage = genre.isEmpty() ? nationalityRepository.findAll(pageable)
				: nationalityRepository.findByLabelContaining(keyword, pageable);
		return PagedResultDto.<NationalityDto>builder()
				.data(nationalityPage.toList().stream().map(nationalityMapper::toDto).toList())
				.totalElements(nationalityPage.getTotalElements()).pageNumber(pageNumber + 1)
				.totalPages(nationalityPage.getTotalPages()).isFirst(nationalityPage.isFirst())
				.isLast(nationalityPage.isLast()).hasNext(nationalityPage.hasNext())
				.hasPrevious(nationalityPage.hasPrevious()).build();

	}

	public PagedResultDto<NationalityDto> getAll(int pageNumber, String keyword) {
		Sort sort = Sort.by("id").ascending();
		pageNumber = pageNumber <= 1 ? 0 : pageNumber - 1;
		Pageable pageable = PageRequest.of(pageNumber, 5, sort);
		Page<Nationality> nationalityPage = keyword.isEmpty() ? nationalityRepository.findAll(pageable)
				: nationalityRepository.findByLabelContaining(keyword, pageable);
		return PagedResultDto.<NationalityDto>builder()
				.data(nationalityPage.toList().stream().map(nationalityMapper::toDto).toList())
				.totalElements(nationalityPage.getTotalElements()).pageNumber(pageNumber + 1)
				.totalPages(nationalityPage.getTotalPages()).isFirst(nationalityPage.isFirst())
				.isLast(nationalityPage.isLast()).hasNext(nationalityPage.hasNext())
				.hasPrevious(nationalityPage.hasPrevious()).build();

	}

	public List<NationalityDto> getAllNoPagination() {
		List<Nationality> nationalities = nationalityRepository.findAll();
		return nationalities.stream().map(nationalityMapper::toDto).toList();
	}

}
