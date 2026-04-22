package ntt.beca.films.media;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "medias", path = "medias")
public interface MediaRepository extends JpaRepository<Media, Long> {

	@Query("SELECT m FROM Media m JOIN Film f  ON f.id = m.film.id ")
	List<Media> findMediasOfFilms(Long id);

}
