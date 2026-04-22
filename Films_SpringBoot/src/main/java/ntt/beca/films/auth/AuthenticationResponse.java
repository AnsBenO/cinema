package ntt.beca.films.auth;

public record AuthenticationResponse(
            String jwt, CurrentUserDto user) {

}
