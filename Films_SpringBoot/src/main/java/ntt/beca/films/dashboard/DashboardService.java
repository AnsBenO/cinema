package ntt.beca.films.dashboard;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ntt.beca.films.rating.FilmRating;
import ntt.beca.films.screening.Screening;
import ntt.beca.films.rating.FilmRatingRepository;
import ntt.beca.films.film.FilmRepository;
import ntt.beca.films.genre.GenreRepository;
import ntt.beca.films.screening.ScreeningRepository;
import ntt.beca.films.user.UserRepository;

@Service
@RequiredArgsConstructor
public class DashboardService {
      private final FilmRepository filmRepository;
      private final GenreRepository genreRepository;
      private final UserRepository userRepository;
      private final ScreeningRepository screeningRepository;
      private final FilmRatingRepository filmRatingRepository;

      public List<Screening> getUpcomingScreenings() {

            return screeningRepository.findUpcomingScreenings();
      }

      public DashboardDataDto getDashboardData() {
            long totalFilms = filmRepository.count();
            long totalGenres = genreRepository.count();
            long totalUsers = userRepository.count();
            long totalScreenings = screeningRepository.count();

            // Calculate average rating
            Double averageRating = filmRatingRepository.findAll().stream()
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
