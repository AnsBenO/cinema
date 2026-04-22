package ntt.beca.films.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ntt.beca.films.shared.security.Role;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

      Page<UserEntity> findByRole(Role role, Pageable pageable);

      Page<UserEntity> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String email,
                  Pageable pageable);

      Optional<UserEntity> findByEmail(String email);

      boolean existsUserEntityByEmail(String email);

      UserEntity findByUsername(String username);

}
