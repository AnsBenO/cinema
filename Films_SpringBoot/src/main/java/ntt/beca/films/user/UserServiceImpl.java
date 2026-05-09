package ntt.beca.films.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ntt.beca.films.shared.security.Role;
import ntt.beca.films.shared.service.PagedResultDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

      private final UserRepository userRepository;
      private final UserMapper userMapper;

      @Override
      public void save(RegistrationDto registrationDto) {
            UserEntity userEntity = userMapper.toEntity(registrationDto);
            userEntity.setRole(Role.ADMIN);
            userRepository.save(userEntity);
      }

      public void update(UserDto userDto) {
            Long userId = userDto.getId();
            if (userId == null) {
                  throw new IllegalArgumentException("User ID is required");
            }
            UserEntity userToUpdate = userRepository.findById(userId).orElseThrow(
                        () -> new IllegalArgumentException("User not found"));
            userToUpdate.setUsername(userDto.getUsername());
            userToUpdate.setEmail(userDto.getEmail());
            userToUpdate.setRole(userDto.getRole());
            userRepository.save(userToUpdate);
      }

      @Override
      public UserDto getOne(@NonNull Long id) {
            UserEntity userEntity = userRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));
            return userMapper.toDto(userEntity);
      }

      @Override
      public PagedResultDto<UserDto> getAll(int pageNumber, String keyword, String role) {
            Pageable pageable = PageRequest.of(pageNumber - 1, 5);

            Page<UserEntity> page;
            if (role != null && !role.isEmpty()) {
                  page = userRepository.findByRole(Role.valueOf(role), pageable);
            } else if (keyword == null || keyword.isBlank()) {
                  page = userRepository.findAll(pageable);
            } else {
                  page = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword,
                              pageable);
            }

            List<UserDto> dtos = page.getContent().stream()
                        .map(userMapper::toDto)
                        .toList();

            return PagedResultDto.<UserDto>builder()
                        .data(dtos)
                        .hasNext(page.hasNext())
                        .hasPrevious(page.hasPrevious())
                        .totalPages(page.getTotalPages())
                        .isFirst(page.isFirst())
                        .isLast(page.isLast())
                        .pageNumber(pageNumber)
                        .build();
      }

      @Override
      public boolean delete(@NonNull Long id) {
            if (userRepository.existsById(id)) {
                  userRepository.deleteById(id);
                  return true;
            }
            return false;
      }

      @Override
      public boolean existsByEmail(String email) {
            return userRepository.existsUserEntityByEmail(email);
      }

      @Override
      public boolean existsByUsername(String username) {
            return userRepository.existsUserEntityByUsername(username);
      }
}
