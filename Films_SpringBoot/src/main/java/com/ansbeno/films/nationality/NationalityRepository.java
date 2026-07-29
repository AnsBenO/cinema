package com.ansbeno.films.nationality;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NationalityRepository extends JpaRepository<Nationality, Long> {

	Page<Nationality> findByLabelContaining(String keyword, Pageable pageable);
}
