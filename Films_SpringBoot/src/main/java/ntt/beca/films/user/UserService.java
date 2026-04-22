package ntt.beca.films.user;

import ntt.beca.films.shared.service.PagedResultDto;

public interface UserService {
      void save(UserDto userDto);

      UserDto getOne(Long id);

      PagedResultDto<UserDto> getAll(int pageNumber, String keyword, String unused);

      boolean delete(Long id);

      boolean existsByEmail(String email);
}