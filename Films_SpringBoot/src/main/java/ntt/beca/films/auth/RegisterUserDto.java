package ntt.beca.films.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserDto(
            @NotBlank(message = "Username is required") @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters") String username,

            @NotBlank(message = "Email is required") @Email(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]{2,}$", message = "Must be a valid email address (e.g. user@example.com)") String email,

            @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters") String password) {
}
