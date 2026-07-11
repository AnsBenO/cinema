package com.ansbeno.films.user;

import com.ansbeno.films.shared.service.PagedResultDto;

public interface UserService {
      void save(RegistrationDto userDto);

      UserDto getOne(Long id);

      PagedResultDto<UserDto> getAll(int pageNumber, String keyword, String unused);

      boolean delete(Long id);

      boolean existsByEmail(String email);

      boolean existsByUsername(String username);
}
