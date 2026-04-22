package ntt.beca.films.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ntt.beca.films.shared.security.Role;
import ntt.beca.films.user.UserEntity;
import ntt.beca.films.user.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
      private final AuthenticationManager authenticationManager;
      private final UserRepository userRepository;
      private final JwtService jwtService;
      private final BCryptPasswordEncoder encoder;

      public AuthenticationResponse login(AuthenticationRequest request) {

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password());

            authenticationManager.authenticate(token);

            UserEntity user = userRepository.findByEmail(request.email()).get();
            String jwt = jwtService.generateToken(user, generateExtraClaims(user));

            return new AuthenticationResponse(jwt, new CurrentUserDto(user.getUsername(), user.getEmail()));
      }

      private Map<String, Object> generateExtraClaims(UserEntity user) {
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("name", user.getEmail());
            extraClaims.put("role", user.getRole().name());
            return extraClaims;

      }

      public AuthenticationResponse register(RegisterUserDto request) {
            UserEntity user = UserEntity.builder()
                        .username(request.username())
                        .email(request.email())
                        .password(encoder.encode(request.password()))
                        .role(Role.CUSTOMER)
                        .build();
            userRepository.save(user);
            String token = jwtService.generateToken(user, generateExtraClaims(user));
            return new AuthenticationResponse(token, new CurrentUserDto(user.getUsername(), user.getEmail()));

      }

      public CurrentUserDto getCurrentUserDetails(String email) {
            UserEntity user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return new CurrentUserDto(user.getUsername(), user.getEmail());
      }
}
