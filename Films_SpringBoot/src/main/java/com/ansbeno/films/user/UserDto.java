package com.ansbeno.films.user;

import java.time.LocalDateTime;

import com.ansbeno.films.shared.security.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
