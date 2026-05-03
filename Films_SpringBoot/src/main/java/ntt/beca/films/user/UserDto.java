package ntt.beca.films.user;

import java.time.LocalDateTime;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.Email;
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
      @NotNull
      private String username;
      @NotNull
      @Email
      private String email;
      @NotNull
      private String password;

      private Role role;
      private LocalDateTime createdAt;
      private LocalDateTime updatedAt;
}