package ntt.beca.films.shared.exception;

public class EmailAlreadyRegisteredException extends UserAlreadyExistsException {
      public EmailAlreadyRegisteredException(String email) {
            super("Email is already registered: " + email);
      }
}
