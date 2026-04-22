package ntt.beca.films.rating;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntt.beca.films.film.Film;
import ntt.beca.films.user.UserEntity;
import ntt.beca.films.film.FilmRepository;
import ntt.beca.films.user.UserRepository;
import ntt.beca.films.auth.JwtService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/film-ratings")
public class ApiFilmRatingController {

      private final FilmRepository filmRepository;

      private final UserRepository userRepository;

      private final FilmRatingRepository filmRatingRepository;

      private final JwtService jwtService;

      /**
       * Submit a film rating.
       */
      @PostMapping
      public ResponseEntity<Map<String, String>> submitRating(@RequestBody RatingPayload rating,
                  HttpServletRequest request) {

            String authHeader = request.getHeader("Authorization");
            String token = authHeader.split(" ")[1];
            String email = jwtService.extractEmail(token);
            Map<String, String> response = new HashMap<>();
            try {
                  // Validate score
                  if (rating.score() < 1 || rating.score() > 10) {
                        response.put("message", "Rating must be between 1 and 10.");
                        return ResponseEntity.badRequest().body(response);
                  }

                  // Authenticate user (simplified user fetch logic for now)
                  UserEntity customer = userRepository.findByEmail(email).orElseThrow();

                  // Fetch film by name
                  Film film = filmRepository.findByTitle(rating.film())
                              .orElseThrow(() -> new RuntimeException("Film not found."));

                  // Map payload to entity
                  FilmRating filmRating = FilmRating.builder()
                              .customer(customer)
                              .film(film)
                              .score(rating.score())
                              .build();

                  // Persist the rating
                  filmRatingRepository.save(filmRating);

                  response.put("message", "Rating submitted successfully.");
                  return ResponseEntity.ok(response);
            } catch (Exception e) {
                  response.put("message", "Could not submit rating: " + e.getMessage());
                  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
      }
}
