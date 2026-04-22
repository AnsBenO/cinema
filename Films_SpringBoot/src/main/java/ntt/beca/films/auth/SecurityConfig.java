package ntt.beca.films.auth;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import ntt.beca.films.shared.security.Role;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
class SecurityConfig {

      private final JwtAuthenticationFilter jwtAuthenticationFilter;

      // JWT-based security for API endpoints
      @Bean
      @Order(1)
      SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
            http
                        .securityMatcher("/api/**")
                        .csrf(AbstractHttpConfigurer::disable)
                        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                        .authorizeHttpRequests(authConfig -> {
                              authConfig.requestMatchers(HttpMethod.POST, "/api/authenticate").permitAll();
                              authConfig.requestMatchers(HttpMethod.POST, "/api/register").permitAll();
                              authConfig.requestMatchers(HttpMethod.GET, "/api/**").permitAll();
                              authConfig.requestMatchers(HttpMethod.POST, "/api/**").authenticated();
                              authConfig.anyRequest().denyAll(); // Deny other requests in this chain
                        })
                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
      }

      @Bean
      @Order(2)
      SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                        .csrf(csrf -> csrf
                                    .ignoringRequestMatchers("/h2-console/**", "/swagger-ui/**", "/api/**"))
                        .headers(headers -> headers
                                    .frameOptions(FrameOptionsConfig::sameOrigin))
                        .authorizeHttpRequests(request -> request
                                    .requestMatchers(
                                                "/dashboard", "/film-ratings", "/films", "/genres", "/halls",
                                                "/nationalities", "/persons", "/screenings", "/users")
                                    .hasAnyAuthority(
                                                Role.ADMIN.getPermissions().stream()
                                                            .map(Enum::name)
                                                            .toArray(String[]::new)))
                        .authorizeHttpRequests(request -> request
                                    .requestMatchers(
                                                "/", "/register", "/webjars/**", "/tailwind/**",
                                                "/register/**", "/login/**", "/css/**", "/js/**",
                                                "/assets/**", "/images/**", "/h2-console/**",
                                                "/swagger-ui/**", "/v3/api-docs*/**", "/error")
                                    .permitAll()
                                    .anyRequest().authenticated())
                        .formLogin(form -> form
                                    .loginPage("/login")
                                    .defaultSuccessUrl("/")
                                    .successHandler((request, response, authentication) -> {
                                          request.getSession().setAttribute("loginSuccessMessage",
                                                      "You have logged in successfully!");
                                          response.sendRedirect("/");
                                    })
                                    .failureUrl("/login?error=true")
                                    .permitAll())

                        .logout(logout -> logout
                                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                    .permitAll());

            return http.build();
      }

      @Bean
      AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
            return configuration.getAuthenticationManager();
      }

      @Bean
      PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
      }

      @Bean
      CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Frontend URL
            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(List.of("*"));
            configuration.setExposedHeaders(List.of("Authorization"));
            configuration.setAllowCredentials(true);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
      }
}
