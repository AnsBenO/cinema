package ntt.beca.films.shared.exception;

public class UsernameAlreadyTakenException extends UserAlreadyExistsException {

      public UsernameAlreadyTakenException(String username) {
            super("Username is already taken: " + username);
      }

}
