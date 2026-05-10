package ntt.beca.films.user;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntt.beca.films.shared.security.Role;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
      private Long id;

      @NotBlank(message = "Username is required")
      private String username;
      @NotBlank(message = "Email is required")
      private String email;
      @NotNull(message = "Role is required")
      private Role role;
      private LocalDateTime createdAt;
      private LocalDateTime updatedAt;
}
