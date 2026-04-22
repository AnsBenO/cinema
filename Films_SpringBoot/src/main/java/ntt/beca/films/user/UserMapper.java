package ntt.beca.films.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

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
                        .id(dto.getId())
                        .username(dto.getUsername())
                        .email(dto.getEmail())
                        .password(dto.getPassword()) // Ensure password handling is secure
                        .role(dto.getRole())
                        .createdAt(dto.getCreatedAt())
                        .updatedAt(dto.getUpdatedAt())
                        .build();
      }
}