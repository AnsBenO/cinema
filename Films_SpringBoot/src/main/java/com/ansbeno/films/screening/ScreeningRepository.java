package com.ansbeno.films.screening;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {

	@Query("""
			SELECT s FROM Screening s
			JOIN s.film f
			WHERE LOWER(f.title) LIKE CONCAT('%', :keyword, '%')
			""")
	Page<Screening> findByFilmTitle(@Param("keyword") String keyword, Pageable pageable);

	@Query("SELECT s FROM Screening s WHERE s.startTime > CURRENT_DATE")
	List<Screening> findUpcomingScreenings();

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT s FROM Screening s WHERE s.id = :id")
	java.util.Optional<Screening> findWithLockById(@Param("id") Long id);

	Page<Screening> findAll(Specification<Screening> spec, Pageable pageable);

}
