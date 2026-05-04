package ntt.beca.films.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ntt.beca.films.shared.base.BaseMapper;

@RequiredArgsConstructor
@Component
public class UserMapper implements BaseMapper<UserEntity, UserDto> {

      private final PasswordEncoder passwordEncoder;

      public UserDto toDto(UserEntity entity) {
            return UserDto.builder()
                        .id(entity.getId())
                        .username(entity.getUsername())
                        .email(entity.getEmail())
                        .role(entity.getRole())
                        .createdAt(entity.getCreatedAt())
                        .updatedAt(entity.getUpdatedAt())
                        .build();
      }

      public UserEntity toEntity(UserDto dto) {
            return UserEntity.builder()
                        .username(dto.getUsername())
                        .email(dto.getEmail())
                        .role(dto.getRole())
                        .build();
      }

      public UserEntity toEntity(RegistrationDto dto) {
            return UserEntity.builder()
                        .username(dto.getUsername())
                        .email(dto.getEmail())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .build();
      }

}
