package ntt.beca.films.hall;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "hall", path = "halls")
public interface HallRepository extends JpaRepository<Hall, Long> {

	@Query("SELECT h FROM Hall h WHERE CAST(h.capacity AS string) LIKE %:keyword%")
	Page<Hall> findByCapacity(String keyword, Pageable pageable);
}
