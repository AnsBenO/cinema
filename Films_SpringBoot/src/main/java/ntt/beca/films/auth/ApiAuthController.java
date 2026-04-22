package ntt.beca.films.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiAuthController {
      private final AuthenticationService authenticationService;

      private final JwtService jwtService;

      @PostMapping("/register")
      ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterUserDto request) {
            return ResponseEntity.ok(authenticationService.register(request));
      }

      @PostMapping("/authenticate")
      ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
            return ResponseEntity.ok(authenticationService.login(request));
      }

      @PostMapping("/user")
      ResponseEntity<CurrentUserDto> currentUser(HttpServletRequest request) {
            String authHeader = request.getHeader("Authorization");
            String token = authHeader.split(" ")[1];
            String email = jwtService.extractEmail(token);

            return ResponseEntity.ok(authenticationService.getCurrentUserDetails(email));
      }

}
