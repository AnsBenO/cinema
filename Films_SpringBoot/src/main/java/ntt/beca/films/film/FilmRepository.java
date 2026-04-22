package ntt.beca.films.film;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "films", path = "films")
public interface FilmRepository extends JpaRepository<Film, Long> {

	@Query("""
			    SELECT f FROM Film f
			    JOIN f.genre g
			    WHERE (:genreLabel IS NULL OR :genreLabel = '' OR g.label = :genreLabel)
			      AND (:keyword IS NULL OR :keyword = '' OR LOWER(f.title) LIKE CONCAT('%', LOWER(:keyword), '%'))
			""")
	Page<Film> findByTitleAndGenre(String keyword, String genreLabel, Pageable pageable);

	Optional<Film> findByTitle(String film);

}
