package ntt.beca.films.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ntt.beca.films.film.Film;
import ntt.beca.films.rating.FilmRating;
import ntt.beca.films.genre.Genre;
import ntt.beca.films.hall.Hall;
import ntt.beca.films.media.Media;
import ntt.beca.films.nationality.Nationality;
import ntt.beca.films.person.Person;
import ntt.beca.films.shared.security.Role;

import ntt.beca.films.screening.Screening;
import ntt.beca.films.user.UserEntity;
import ntt.beca.films.rating.FilmRatingRepository;
import ntt.beca.films.film.FilmRepository;
import ntt.beca.films.genre.GenreRepository;
import ntt.beca.films.hall.HallRepository;
import ntt.beca.films.media.MediaRepository;
import ntt.beca.films.media.MediaType;
import ntt.beca.films.nationality.NationalityRepository;
import ntt.beca.films.person.PersonRepository;
import ntt.beca.films.person.PersonType;
import ntt.beca.films.screening.ScreeningRepository;
import ntt.beca.films.user.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class Initializer {

      @Value("${image.directory}")
      String imageBasePath;

      private final PasswordEncoder passwordEncoder;

      private final GenreRepository genreRepository;
      private final NationalityRepository nationalityRepository;
      private final PersonRepository personRepository;
      private final FilmRepository filmRepository;
      private final HallRepository theaterRepository;
      private final MediaRepository mediaRepository;
      private final ScreeningRepository screeningRepository;
      private final FilmRatingRepository filmRatingRepository;
      private final UserRepository userRepository;

      @Transactional
      @PostConstruct
      void loadData() {
            if (filmRepository.count() > 0) {
                  log.info("Data already exists. Skipping initialization.");
                  return;
            }

            log.info("Starting data initialization...");

            // Insert Genres
            Genre action = new Genre("Action", new ArrayList<>());
            Genre drama = new Genre("Drama", new ArrayList<>());
            Genre scienceFiction = new Genre("Science Fiction", new ArrayList<>());
            Genre war = new Genre("War", new ArrayList<>());

            // Save genres first
            genreRepository.saveAll(List.of(action, drama, scienceFiction, war));

            // Insert Nationalities
            Nationality american = new Nationality("American", new ArrayList<>());
            Nationality british = new Nationality("British", new ArrayList<>());
            nationalityRepository.saveAll(List.of(american, british));

            // Insert Persons (Actors and Directors)
            Person director = new Person(
                        "Nolan", "Christopher", null, LocalDate.of(1970, 7, 30), PersonType.DIRECTOR, american,
                        new ArrayList<>(), new ArrayList<>());
            Person actor = new Person(
                        "DiCaprio", "Leonardo", null, LocalDate.of(1974, 11, 11), PersonType.ACTOR, american,
                        new ArrayList<>(), new ArrayList<>());
            personRepository.saveAll(List.of(director, actor));

            // Insert Films
            Film inception = Film.builder().title("inception").actors(List.of(actor)).director(director)
                        .imageUrl(imageBasePath + "inception.jpg")
                        .description("Cobb, a skilled thief, is hired to steal valuable secrets from people's dreams.")
                        .genre(action).duration(148).screenings(new ArrayList<>()).year(2010).nationality(american)
                        .build();

            Film interstellar = Film.builder().title("Interstellar").duration(169).year(2014).genre(scienceFiction)
                        .imageUrl(imageBasePath + "interstellar.jpg")
                        .description(
                                    "As Earth's resources dwindle, a team of explorers must travel through a wormhole in search of a new home.")
                        .nationality(american).director(director).actors(List.of(actor)).screenings(new ArrayList<>())
                        .build();

            Film dunkirk = Film.builder().title("Dunkirk").duration(106).year(2017).genre(war).nationality(british)
                        .imageUrl(imageBasePath + "dunkirk.jpg")
                        .description(
                                    "The evacuation of Allied soldiers from the beaches of Dunkirk, France, during World War II.")
                        .director(director).screenings(new ArrayList<>()).build();
            // Save films after genres and persons are saved
            filmRepository.saveAll(List.of(inception, interstellar, dunkirk));

            // Insert Theaters
            Hall hall1 = new Hall(1, 150, new ArrayList<>());
            Hall hall2 = new Hall(2, 200, new ArrayList<>());
            Hall hall3 = new Hall(3, 180, new ArrayList<>());
            theaterRepository.saveAll(List.of(hall1, hall2, hall3));

            // Insert Screenings with dynamic future dates
            LocalDateTime now = LocalDateTime.now();

            Screening screening1 = Screening.builder()
                        .film(inception)
                        .hall(hall1)
                        .startTime(now.plusDays(1).withHour(18).withMinute(0).withSecond(0).withNano(0))
                        .endTime(now.plusDays(1).withHour(20).withMinute(30).withSecond(0).withNano(0))
                        .build();
            Screening screening2 = Screening.builder()
                        .film(interstellar)
                        .hall(hall2)
                        .startTime(now.plusDays(2).withHour(15).withMinute(0).withSecond(0).withNano(0))
                        .endTime(now.plusDays(2).withHour(17).withMinute(50).withSecond(0).withNano(0))
                        .build();
            Screening screening3 = Screening.builder()
                        .film(dunkirk)
                        .hall(hall3)
                        .startTime(now.plusDays(3).withHour(20).withMinute(0).withSecond(0).withNano(0))
                        .endTime(now.plusDays(3).withHour(21).withMinute(45).withSecond(0).withNano(0))
                        .build();
            Screening screening4 = Screening.builder()
                        .film(inception)
                        .hall(hall2)
                        .startTime(now.plusDays(4).withHour(15).withMinute(0).withSecond(0).withNano(0))
                        .endTime(now.plusDays(4).withHour(17).withMinute(30).withSecond(0).withNano(0))
                        .build();
            Screening screening5 = Screening.builder()
                        .film(dunkirk)
                        .hall(hall1)
                        .startTime(now.plusDays(5).withHour(18).withMinute(0).withSecond(0).withNano(0))
                        .endTime(now.plusDays(5).withHour(20).withMinute(45).withSecond(0).withNano(0))
                        .build();
            Screening screening6 = Screening.builder()
                        .film(interstellar)
                        .hall(hall3)
                        .startTime(now.plusDays(6).withHour(20).withMinute(0).withSecond(0).withNano(0))
                        .endTime(now.plusDays(6).withHour(22).withMinute(50).withSecond(0).withNano(0))
                        .build();

            screeningRepository
                        .saveAll(List.of(screening1, screening2, screening3, screening4, screening5, screening6));

            // Insert Media
            Media trailer = Media.builder()
                        .film(inception)
                        .media("https://example.com/trailer.mp4")
                        .mediaType(MediaType.VIDEO)
                        .build();
            Media poster = Media.builder()
                        .film(inception)
                        .media("https://example.com/poster.jpg")
                        .mediaType(MediaType.IMAGE)
                        .build();
            Media script = Media.builder()
                        .film(interstellar)
                        .media("https://example.com/script.pdf")
                        .mediaType(MediaType.DOCUMENT)
                        .build();
            mediaRepository.saveAll(List.of(trailer, poster, script));

            // Insert Users (Customers)
            UserEntity customer1 = UserEntity.builder()
                        .username("john_doe")
                        .email("john.doe@example.com")
                        .password(passwordEncoder.encode("1234"))
                        .role(Role.CUSTOMER)
                        .build();

            UserEntity customer2 = UserEntity.builder()
                        .username("jane_smith")
                        .email("jane.smith@example.com")
                        .password(passwordEncoder.encode("1234"))
                        .role(Role.CUSTOMER)
                        .build();
            UserEntity admin = UserEntity.builder()
                        .username("admin")
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("1234"))
                        .role(Role.ADMIN)
                        .build();

            userRepository.saveAll(List.of(customer1, customer2, admin));

            // Insert Ratings with Users assigned to FilmRating
            FilmRating rating1 = FilmRating.builder()
                        .film(inception)
                        .score(5)
                        .customer(customer1) // Assign customer1
                        .build();

            FilmRating rating2 = FilmRating.builder()
                        .film(interstellar)
                        .score(4)
                        .customer(customer2) // Assign customer2
                        .build();

            filmRatingRepository.saveAll(List.of(rating1, rating2));

            log.info("Data initialization completed.");
      }

}
