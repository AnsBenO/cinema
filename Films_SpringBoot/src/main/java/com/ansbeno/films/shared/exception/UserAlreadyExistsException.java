package com.ansbeno.films.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public abstract class UserAlreadyExistsException extends RuntimeException {
      protected UserAlreadyExistsException(String message) {
            super(message);
      }
}
