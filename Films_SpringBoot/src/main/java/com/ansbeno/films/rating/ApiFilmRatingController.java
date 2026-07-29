package com.ansbeno.films.rating;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ansbeno.films.auth.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/film-ratings")
@Slf4j
public class ApiFilmRatingController {

      private final FilmRatingService filmRatingService;

      private final JwtService jwtService;

      /**
       * Submit a film rating.
       */
      @PostMapping
      public ResponseEntity<Map<String, String>> submitRating(@Valid @RequestBody RatingPayload rating,
                  HttpServletRequest request) {

            String authHeader = request.getHeader("Authorization");
            String token = authHeader.split(" ")[1];
            String email = jwtService.extractEmail(token);
            Map<String, String> response = new HashMap<>();
            try {
                  filmRatingService.submitRating(email, rating);
                  response.put("message", "Rating submitted successfully.");
                  return ResponseEntity.ok(response);

            } catch (Exception e) {
                  response.put("message", "Could not submit rating: " + e.getMessage());
                  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
      }

      // get Rating by user email and film title
      @GetMapping("/{title}/{userEmail}")
      public Map<String, Integer> getRating(@PathVariable String title, @PathVariable String userEmail) {

            // return {score: integer}
            Integer rating = filmRatingService.getRatingByFilmAndUser(title, userEmail);

            Map<String, Integer> response = new HashMap<>();

            log.info("Rating for film {} by user {} is {}", title, userEmail, rating);
            response.put("score", rating);
            return response;
      }
}
