package ntt.beca.films.nationality;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ntt.beca.films.shared.service.CrudService;
import ntt.beca.films.shared.service.PagedResultDto;

@Service
public class NationalityService implements CrudService<Nationality, Long> {

	private NationalityRepository nationalityRepository;

	// constructor and repository injection
	public NationalityService(NationalityRepository nationalityRepository) {
		this.nationalityRepository = nationalityRepository;
	}

	// saving method
	@Override
	public void save(Nationality t) {
		nationalityRepository.save(t);
	}

	// getting one nationality by Id
	@Override
	public Nationality getOne(Long id) {
		return nationalityRepository.findById(id).get();
	}

	// deleting one nationality by Id
	@Override
	public boolean delete(Long id) {
		if (nationalityRepository.existsById(id)) {
			nationalityRepository.deleteById(id);
			return true;
		}
		return false;
	}

	// getting all the nationalities using pagination
	@Override
	public PagedResultDto<Nationality> getAll(int pageNumber, String keyword, String genre) {
		Sort sort = Sort.by("id").ascending();
		pageNumber = pageNumber <= 1 ? 0 : pageNumber - 1;
		Pageable pageable = PageRequest.of(pageNumber, 5, sort);
		Page<Nationality> nationalityPage = genre.isEmpty() ? nationalityRepository.findAll(pageable)
				: nationalityRepository.findByLabelContaining(keyword, pageable);
		return PagedResultDto.<Nationality>builder().data(nationalityPage.toList())
				.totalElements(nationalityPage.getTotalElements()).pageNumber(pageNumber + 1)
				.totalPages(nationalityPage.getTotalPages()).isFirst(nationalityPage.isFirst())
				.isLast(nationalityPage.isLast()).hasNext(nationalityPage.hasNext())
				.hasPrevious(nationalityPage.hasPrevious()).build();

	}

	public PagedResultDto<Nationality> getAll(int pageNumber, String keyword) {
		Sort sort = Sort.by("id").ascending();
		pageNumber = pageNumber <= 1 ? 0 : pageNumber - 1;
		Pageable pageable = PageRequest.of(pageNumber, 5, sort);
		Page<Nationality> nationalityPage = keyword.isEmpty() ? nationalityRepository.findAll(pageable)
				: nationalityRepository.findByLabelContaining(keyword, pageable);
		return PagedResultDto.<Nationality>builder().data(nationalityPage.toList())
				.totalElements(nationalityPage.getTotalElements()).pageNumber(pageNumber + 1)
				.totalPages(nationalityPage.getTotalPages()).isFirst(nationalityPage.isFirst())
				.isLast(nationalityPage.isLast()).hasNext(nationalityPage.hasNext())
				.hasPrevious(nationalityPage.hasPrevious()).build();

	}

	public List<Nationality> getAllNoPagination() {
		return nationalityRepository.findAll();
	}

}
