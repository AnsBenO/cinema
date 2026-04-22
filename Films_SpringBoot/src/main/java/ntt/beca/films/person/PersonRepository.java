package ntt.beca.films.person;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "persons", path = "persons")
public interface PersonRepository extends JpaRepository<Person, Long> {

	@Query("""
			SELECT p FROM Person p
			WHERE LOWER(p.firstName) LIKE CONCAT('%', LOWER(:keyword), '%')
			OR LOWER(p.lastName) LIKE CONCAT('%', LOWER(:keyword), '%')
			""")
	Page<Person> findByFirstNameOrLastName(@Param("keyword") String keyword, Pageable pageable);

	List<Person> findByPersonType(PersonType personType);

	@Query("""
			    SELECT p FROM Person p
			    WHERE (:keyword IS NULL OR LOWER(p.firstName) LIKE CONCAT('%', LOWER(:keyword), '%') OR LOWER(p.lastName) LIKE CONCAT('%', LOWER(:keyword), '%'))
			    AND (:personType IS NULL OR p.personType = :personType)
			""")
	Page<Person> findByKeywordAndPersonType(@Param("keyword") String keyword,
			@Param("personType") PersonType personType, Pageable pageable);

}
