package com.ansbeno.films.shared.exception;

public class UsernameAlreadyTakenException extends UserAlreadyExistsException {

      public UsernameAlreadyTakenException(String username) {
            super("Username is already taken: " + username);
      }

}
