package com.ansbeno.films.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ansbeno.films.shared.exception.EmailAlreadyRegisteredException;
import com.ansbeno.films.shared.exception.UsernameAlreadyTakenException;
import com.ansbeno.films.shared.security.Role;
import com.ansbeno.films.user.UserEntity;
import com.ansbeno.films.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
      private final AuthenticationManager authenticationManager;
      private final UserRepository userRepository;
      private final JwtService jwtService;
      private final PasswordEncoder encoder;

      public AuthenticationResponse login(AuthenticationRequest request) {

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password());

            authenticationManager.authenticate(token);

            UserEntity user = userRepository.findByEmail(request.email()).orElseThrow(
                        () -> new UsernameNotFoundException("User not found"));
            String jwt = jwtService.generateToken(user, generateExtraClaims(user));

            return new AuthenticationResponse(jwt, new CurrentUserDto(user.getUsername(), user.getEmail()));
      }

      private Map<String, Object> generateExtraClaims(UserEntity user) {
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("name", user.getUsername());
            extraClaims.put("role", user.getRole().name());
            return extraClaims;

      }

      public AuthenticationResponse register(RegisterUserDto request) {

            if (userRepository.existsUserEntityByEmail(request.email())) {
                  throw new EmailAlreadyRegisteredException(request.email());
            }

            if (userRepository.existsUserEntityByUsername(request.username())) {
                  throw new UsernameAlreadyTakenException(request.username());
            }

            UserEntity user = UserEntity.builder()
                        .username(request.username())
                        .email(request.email())
                        .password(encoder.encode(request.password()))
                        .role(Role.CUSTOMER)
                        .build();

            userRepository.save(Objects.requireNonNull(user));
            String token = jwtService.generateToken(user, generateExtraClaims(user));
            return new AuthenticationResponse(token, new CurrentUserDto(user.getUsername(), user.getEmail()));

      }

      public CurrentUserDto getCurrentUserDetails(String email) {
            UserEntity user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return new CurrentUserDto(user.getUsername(), user.getEmail());
      }
}
