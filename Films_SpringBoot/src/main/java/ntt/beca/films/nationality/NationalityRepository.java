package ntt.beca.films.nationality;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "nationalities", path = "nationalities")
public interface NationalityRepository extends JpaRepository<Nationality, Long> {

	Page<Nationality> findByLabelContaining(String keyword, Pageable pageable);
}
