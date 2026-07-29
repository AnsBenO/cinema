package com.ansbeno.films.shared.exception;

public class BookingCapacityExceededException extends RuntimeException {

      private static final long serialVersionUID = 1L;

      public BookingCapacityExceededException(String message) {
            super(message);
      }
}
