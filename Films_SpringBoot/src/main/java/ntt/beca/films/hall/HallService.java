package ntt.beca.films.hall;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ntt.beca.films.shared.service.CrudService;
import ntt.beca.films.shared.service.PagedResultDto;

@Service
public class HallService implements CrudService<Hall, Long> {

	private HallRepository hallRepository;

	// constructor and repository injection
	public HallService(HallRepository hallRepository) {
		this.hallRepository = hallRepository;
	}

	// saving method
	@Override
	public void save(Hall t) {
		hallRepository.save(t);
	}

	// getting one hall by Id
	@Override
	public Hall getOne(Long id) {
		return hallRepository.findById(id).get();
	}

	// deleting one hall by Id
	@Override
	public boolean delete(Long id) {
		if (hallRepository.existsById(id)) {
			hallRepository.deleteById(id);
			return true;
		}
		return false;
	}

	// getting all the hall using pagination
	@Override
	public PagedResultDto<Hall> getAll(int pageNumber, String keyword, String screaning) {
		return null;
	}

	public PagedResultDto<Hall> getAll(int pageNumber, String keyword) {
		Sort sort = Sort.by("id").ascending();
		pageNumber = pageNumber <= 1 ? 0 : pageNumber - 1;
		Pageable pageable = PageRequest.of(pageNumber, 5, sort);
		Page<Hall> hallPage = hallRepository.findByCapacity(keyword, pageable);
		return PagedResultDto.<Hall>builder().data(hallPage.toList())
				.totalElements(hallPage.getTotalElements()).pageNumber(pageNumber + 1)
				.totalPages(hallPage.getTotalPages()).isFirst(hallPage.isFirst())
				.isLast(hallPage.isLast()).hasNext(hallPage.hasNext())
				.hasPrevious(hallPage.hasPrevious()).build();

	}

	public List<Hall> getAllNoPagination() {
		return hallRepository.findAll();
	}

}
