package ntt.beca.films.screening;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import ntt.beca.films.film.Film;
import ntt.beca.films.hall.Hall;
import ntt.beca.films.shared.service.CrudService;
import ntt.beca.films.shared.service.PagedResultDto;

@Service
public class ScreeningService implements CrudService<Screening, Long> {

	private ScreeningRepository screeningRepository;

	// constructor and repository injection
	public ScreeningService(ScreeningRepository screeningRepository) {
		this.screeningRepository = screeningRepository;
	}

	// saving method
	@Override
	public void save(Screening t) {
		screeningRepository.save(t);
	}

	// getting one screening by Id
	@Override
	public Screening getOne(Long id) {
		return screeningRepository.findById(id).get();
	}

	// deleting one screening by Id
	@Override
	public boolean delete(Long id) {
		if (screeningRepository.existsById(id)) {
			screeningRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public PagedResultDto<Screening> getAll(int pageNumber, String keyword, String genre) {
		Sort sort = Sort.by("id").ascending();
		pageNumber = pageNumber <= 1 ? 0 : pageNumber - 1;
		Pageable pageable = PageRequest.of(pageNumber, 5, sort);
		Page<Screening> screeningPage = genre.isEmpty() ? screeningRepository.findAll(pageable)
				: screeningRepository.findByFilmTitle(keyword, pageable);
		return PagedResultDto.<Screening>builder().data(screeningPage.toList())
				.totalElements(screeningPage.getTotalElements()).pageNumber(pageNumber + 1)
				.totalPages(screeningPage.getTotalPages()).isFirst(screeningPage.isFirst())
				.isLast(screeningPage.isLast()).hasNext(screeningPage.hasNext())
				.hasPrevious(screeningPage.hasPrevious()).build();

	}

	// getting all the screenings using pagination
	public PagedResultDto<Screening> getAllByDate(String filmTitle, LocalDate date, Integer hallNumber,
			int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, 5, Sort.by("startTime").ascending());

		Specification<Screening> spec = (root, query, cb) -> {
			Predicate predicate = cb.conjunction();

			// Filter by specific date (start and end of day)
			if (date != null) {
				LocalDateTime startOfDay = date.atStartOfDay();
				LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
				Predicate withinDateRange = cb.between(root.get("startTime"), startOfDay, endOfDay);
				predicate = cb.and(predicate, withinDateRange);
			}

			// Filter by hall number if provided
			if (hallNumber != null && hallNumber != 0) {
				Join<Screening, Hall> hallJoin = root.join("hall");
				predicate = cb.and(predicate, cb.equal(hallJoin.get("number"), hallNumber));
			}

			// Filter by film title if provided
			if (filmTitle != null && !filmTitle.isEmpty()) {
				Join<Screening, Film> filmJoin = root.join("film"); // Assuming the association is named 'film'
				Predicate filmTitlePredicate = cb.like(cb.lower(filmJoin.get("title")),
						"%" + filmTitle.toLowerCase() + "%");
				predicate = cb.and(predicate, filmTitlePredicate);
			}

			return predicate;
		};

		Page<Screening> screeningPage = screeningRepository.findAll(spec, pageable);
		return PagedResultDto.<Screening>builder()
				.data(screeningPage.toList())
				.totalElements(screeningPage.getTotalElements())
				.pageNumber(pageNumber + 1) // Return 1-based page number
				.totalPages(screeningPage.getTotalPages())
				.isFirst(screeningPage.isFirst())
				.isLast(screeningPage.isLast())
				.hasNext(screeningPage.hasNext())
				.hasPrevious(screeningPage.hasPrevious())
				.build();
	}

}
