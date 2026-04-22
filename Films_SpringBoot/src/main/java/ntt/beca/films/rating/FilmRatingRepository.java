package ntt.beca.films.rating;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FilmRatingRepository extends JpaRepository<FilmRating, Long> {

	@Query("""
			SELECT fr FROM FilmRating fr
			JOIN fr.film f WHERE f.title LIKE %:keyword%
			""")
	Page<FilmRating> findByFilmTitle(String keyword, Pageable pageable);

}