package com.ansbeno.films.genre;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GenreRepository extends JpaRepository<Genre, Long> {

	Page<Genre> findByLabelContaining(String keyword, Pageable pageable);

	@Query("SELECT g FROM Genre g")
	List<Genre> findAllNoPagination();
}
