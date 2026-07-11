package com.ansbeno.films.dashboard;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.ansbeno.films.film.FilmRepository;
import com.ansbeno.films.genre.GenreRepository;
import com.ansbeno.films.rating.FilmRating;
import com.ansbeno.films.rating.FilmRatingRepository;
import com.ansbeno.films.screening.Screening;
import com.ansbeno.films.screening.ScreeningDto;
import com.ansbeno.films.screening.ScreeningMapper;
import com.ansbeno.films.screening.ScreeningRepository;
import com.ansbeno.films.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {
      private final FilmRepository filmRepository;
      private final GenreRepository genreRepository;
      private final UserRepository userRepository;
      private final ScreeningRepository screeningRepository;
      private final FilmRatingRepository filmRatingRepository;
      private final ScreeningMapper screeningMapper;

      public List<ScreeningDto> getUpcomingScreenings() {
            List<Screening> upcomingScreenings = screeningRepository.findUpcomingScreenings();

            return upcomingScreenings.stream().map(screeningMapper::toDto).toList();
      }

      public DashboardDataDto getDashboardData() {
            long totalFilms = filmRepository.count();
            long totalGenres = genreRepository.count();
            long totalUsers = userRepository.count();
            long totalScreenings = screeningRepository.count();

            // Calculate average rating
            Double averageRating = filmRatingRepository.findAll().stream()
                        .filter(Objects::nonNull)
                        .mapToInt(FilmRating::getScore)
                        .average()
                        .orElse(0.0);

            return DashboardDataDto.builder()
                        .totalFilms(totalFilms)
                        .totalGenres(totalGenres)
                        .totalUsers(totalUsers)
                        .totalScreenings(totalScreenings)
                        .averageRating(averageRating)
                        .build();
      }
}
