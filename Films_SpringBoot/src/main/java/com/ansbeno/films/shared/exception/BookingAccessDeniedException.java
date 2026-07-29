package com.ansbeno.films.shared.exception;

public class BookingAccessDeniedException extends RuntimeException {

      private static final long serialVersionUID = 1L;

      public BookingAccessDeniedException(String message) {
            super(message);
      }
}
