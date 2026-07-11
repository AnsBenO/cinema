package com.ansbeno.films.auth;

public record AuthenticationResponse(
            String jwt, CurrentUserDto user) {

}
