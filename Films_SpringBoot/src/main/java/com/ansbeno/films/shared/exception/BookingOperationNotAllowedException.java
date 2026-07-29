package com.ansbeno.films.shared.exception;

public class BookingOperationNotAllowedException extends RuntimeException {

      private static final long serialVersionUID = 1L;

      public BookingOperationNotAllowedException(String message) {
            super(message);
      }
}
