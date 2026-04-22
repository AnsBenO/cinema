package ntt.beca.films.user;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntt.beca.films.shared.security.Role;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
      private Long id;
      private String username;
      private String email;
      private String password;
      private Role role;
      private LocalDateTime createdAt;
      private LocalDateTime updatedAt;
}