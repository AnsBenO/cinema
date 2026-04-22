package ntt.beca.films.screening;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "screenings", path = "screenings", excerptProjection = ScreeningProjection.class)
public interface ScreeningRepository extends JpaRepository<Screening, Long> {

	@Query("""
			SELECT s FROM Screening s
			JOIN s.film f
			WHERE LOWER(f.title) LIKE CONCAT('%', :keyword, '%')
			""")
	Page<Screening> findByFilmTitle(@Param("keyword") String keyword, Pageable pageable);

	@Query("SELECT s FROM Screening s WHERE s.startTime > CURRENT_DATE")
	List<Screening> findUpcomingScreenings();

	Page<Screening> findAll(Specification<Screening> spec, Pageable pageable);

}
