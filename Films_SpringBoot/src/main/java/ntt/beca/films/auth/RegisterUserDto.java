package ntt.beca.films.auth;

public record RegisterUserDto(
            String username,
            String email,
            String password) {
}
