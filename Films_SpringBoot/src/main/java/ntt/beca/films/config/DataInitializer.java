package ntt.beca.films.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

@SuppressWarnings("all")
@Slf4j
@RequiredArgsConstructor
@Component
class DataInitializer {

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

            // ----------------------------------------------------------------
            // 15 Genres (page-size = 5 → 3 full pages)
            // ----------------------------------------------------------------
            Genre action = new Genre("Action", new ArrayList<>());
            Genre drama = new Genre("Drama", new ArrayList<>());
            Genre scienceFiction = new Genre("Science Fiction", new ArrayList<>());
            Genre war = new Genre("War", new ArrayList<>());
            Genre comedy = new Genre("Comedy", new ArrayList<>());
            Genre thriller = new Genre("Thriller", new ArrayList<>());
            Genre horror = new Genre("Horror", new ArrayList<>());
            Genre romance = new Genre("Romance", new ArrayList<>());
            Genre animation = new Genre("Animation", new ArrayList<>());
            Genre documentary = new Genre("Documentary", new ArrayList<>());
            Genre fantasy = new Genre("Fantasy", new ArrayList<>());
            Genre mystery = new Genre("Mystery", new ArrayList<>());
            Genre adventure = new Genre("Adventure", new ArrayList<>());
            Genre crime = new Genre("Crime", new ArrayList<>());
            Genre biography = new Genre("Biography", new ArrayList<>());

            genreRepository.saveAll(List.of(
                        action, drama, scienceFiction, war, comedy,
                        thriller, horror, romance, animation, documentary,
                        fantasy, mystery, adventure, crime, biography));

            // ----------------------------------------------------------------
            // 15 Nationalities
            // ----------------------------------------------------------------
            Nationality american = new Nationality("American", new ArrayList<>());
            Nationality british = new Nationality("British", new ArrayList<>());
            Nationality french = new Nationality("French", new ArrayList<>());
            Nationality german = new Nationality("German", new ArrayList<>());
            Nationality italian = new Nationality("Italian", new ArrayList<>());
            Nationality spanish = new Nationality("Spanish", new ArrayList<>());
            Nationality japanese = new Nationality("Japanese", new ArrayList<>());
            Nationality korean = new Nationality("Korean", new ArrayList<>());
            Nationality canadian = new Nationality("Canadian", new ArrayList<>());
            Nationality australian = new Nationality("Australian", new ArrayList<>());
            Nationality mexican = new Nationality("Mexican", new ArrayList<>());
            Nationality brazilian = new Nationality("Brazilian", new ArrayList<>());
            Nationality indian = new Nationality("Indian", new ArrayList<>());
            Nationality chinese = new Nationality("Chinese", new ArrayList<>());
            Nationality swedish = new Nationality("Swedish", new ArrayList<>());

            nationalityRepository.saveAll(List.of(
                        american, british, french, german, italian,
                        spanish, japanese, korean, canadian, australian,
                        mexican, brazilian, indian, chinese, swedish));

            // ----------------------------------------------------------------
            // 15 Persons (mix of actors and directors)
            // ----------------------------------------------------------------
            Person nolanDir = new Person("Nolan", "Christopher", null, LocalDate.of(1970, 7, 30),
                        PersonType.DIRECTOR, british, new ArrayList<>(), new ArrayList<>());
            Person spielbergDir = new Person("Spielberg", "Steven", null, LocalDate.of(1946, 12, 18),
                        PersonType.DIRECTOR, american, new ArrayList<>(), new ArrayList<>());
            Person scottDir = new Person("Scott", "Ridley", null, LocalDate.of(1937, 11, 30),
                        PersonType.DIRECTOR, british, new ArrayList<>(), new ArrayList<>());
            Person finchDir = new Person("Fincher", "David", null, LocalDate.of(1962, 8, 28),
                        PersonType.DIRECTOR, american, new ArrayList<>(), new ArrayList<>());
            Person villeneuveDir = new Person("Villeneuve", "Denis", null, LocalDate.of(1967, 10, 3),
                        PersonType.DIRECTOR, canadian, new ArrayList<>(), new ArrayList<>());
            Person andersonDir = new Person("Anderson", "Wes", null, LocalDate.of(1969, 5, 1),
                        PersonType.DIRECTOR, american, new ArrayList<>(), new ArrayList<>());
            Person dicaprioAct = new Person("DiCaprio", "Leonardo", null, LocalDate.of(1974, 11, 11),
                        PersonType.ACTOR, american, new ArrayList<>(), new ArrayList<>());
            Person hardyAct = new Person("Hardy", "Tom", null, LocalDate.of(1977, 9, 15),
                        PersonType.ACTOR, british, new ArrayList<>(), new ArrayList<>());
            Person streepAct = new Person("Streep", "Meryl", null, LocalDate.of(1949, 6, 22),
                        PersonType.ACTOR, american, new ArrayList<>(), new ArrayList<>());
            Person pittAct = new Person("Pitt", "Brad", null, LocalDate.of(1963, 12, 18),
                        PersonType.ACTOR, american, new ArrayList<>(), new ArrayList<>());
            Person blanchettAct = new Person("Blanchett", "Cate", null, LocalDate.of(1969, 5, 14),
                        PersonType.ACTOR, australian, new ArrayList<>(), new ArrayList<>());
            Person cruzAct = new Person("Cruz", "Penélope", null, LocalDate.of(1974, 4, 28),
                        PersonType.ACTOR, spanish, new ArrayList<>(), new ArrayList<>());
            Person washingtonAct = new Person("Washington", "Denzel", null, LocalDate.of(1954, 12, 28),
                        PersonType.ACTOR, american, new ArrayList<>(), new ArrayList<>());
            Person hanksAct = new Person("Hanks", "Tom", null, LocalDate.of(1956, 7, 9),
                        PersonType.ACTOR, american, new ArrayList<>(), new ArrayList<>());
            Person damonAct = new Person("Damon", "Matt", null, LocalDate.of(1970, 10, 8),
                        PersonType.ACTOR, american, new ArrayList<>(), new ArrayList<>());

            personRepository.saveAll(List.of(
                        nolanDir, spielbergDir, scottDir, finchDir, villeneuveDir, andersonDir,
                        dicaprioAct, hardyAct, streepAct, pittAct,
                        blanchettAct, cruzAct, washingtonAct, hanksAct, damonAct));

            // ----------------------------------------------------------------
            // 15 Films
            // ----------------------------------------------------------------
            Film inception = Film.builder().title("Inception").actors(List.of(dicaprioAct, hardyAct))
                        .director(nolanDir).imageUrl(imageBasePath + "inception.jpg")
                        .description(
                                    "Cobb, a skilled thief, steals corporate secrets through dream-sharing technology.")
                        .genre(action).duration(148).screenings(new ArrayList<>()).year(2010).nationality(american)
                        .build();

            Film interstellar = Film.builder().title("Interstellar").duration(169).year(2014).genre(scienceFiction)
                        .imageUrl(imageBasePath + "interstellar.jpg")
                        .description(
                                    "A team of explorers travel through a wormhole in search of a new home for humanity.")
                        .nationality(american).director(nolanDir).actors(List.of(damonAct))
                        .screenings(new ArrayList<>())
                        .build();

            Film dunkirk = Film.builder().title("Dunkirk").duration(106).year(2017).genre(war).nationality(british)
                        .imageUrl(imageBasePath + "dunkirk.jpg")
                        .description("Allied soldiers are evacuated from the beaches of Dunkirk during World War II.")
                        .director(nolanDir).actors(List.of(hardyAct)).screenings(new ArrayList<>()).build();

            Film gladiator = Film.builder().title("Gladiator").duration(155).year(2000).genre(action)
                        .nationality(american)
                        .imageUrl(imageBasePath + "inception.jpg")
                        .description("A betrayed Roman general seeks revenge against the corrupt emperor.")
                        .director(scottDir).actors(List.of(blanchettAct)).screenings(new ArrayList<>()).build();

            Film bladeRunner = Film.builder().title("Blade Runner 2049").duration(164).year(2017).genre(scienceFiction)
                        .nationality(american)
                        .imageUrl(imageBasePath + "inception.jpg")
                        .description("A blade runner uncovers a long-buried secret that threatens society.")
                        .director(villeneuveDir).actors(List.of(damonAct)).screenings(new ArrayList<>()).build();

            Film fightClub = Film.builder().title("Fight Club").duration(139).year(1999).genre(drama)
                        .nationality(american)
                        .imageUrl(imageBasePath + "inception.jpg")
                        .description(
                                    "An insomniac office worker forms an underground fighting club with a soap salesman.")
                        .director(finchDir).actors(List.of(pittAct)).screenings(new ArrayList<>()).build();

            Film theRevenant = Film.builder().title("The Revenant").duration(156).year(2015).genre(adventure)
                        .nationality(american)
                        .imageUrl(imageBasePath + "inception.jpg")
                        .description(
                                    "A frontiersman on a fur trading expedition fights for survival after being mauled.")
                        .director(scottDir).actors(List.of(dicaprioAct)).screenings(new ArrayList<>()).build();

            Film schindlersList = Film.builder().title("Schindler's List").duration(195).year(1993).genre(drama)
                        .nationality(american)
                        .imageUrl(imageBasePath + "inception.jpg")
                        .description("During WWII, Oskar Schindler saves more than a thousand Jewish refugees.")
                        .director(spielbergDir).actors(List.of(hanksAct)).screenings(new ArrayList<>()).build();

            Film castAway = Film.builder().title("Cast Away").duration(143).year(2000).genre(drama)
                        .nationality(american)
                        .imageUrl(imageBasePath + "inception.jpg")
                        .description("A FedEx employee is stranded on an uninhabited island after a plane crash.")
                        .director(spielbergDir).actors(List.of(hanksAct)).screenings(new ArrayList<>()).build();

            Film theMartian = Film.builder().title("The Martian").duration(144).year(2015).genre(scienceFiction)
                        .nationality(american)
                        .imageUrl(imageBasePath + "inception.jpg")
                        .description("An astronaut stranded on Mars must improvise to survive until rescue arrives.")
                        .director(scottDir).actors(List.of(damonAct)).screenings(new ArrayList<>()).build();

            Film trainspotting = Film.builder().title("Trainspotting").duration(94).year(1996).genre(drama)
                        .nationality(british)
                        .imageUrl(imageBasePath + "inception.jpg")
                        .description("A young man and his friends struggle with drug addiction in Edinburgh.")
                        .director(finchDir).actors(List.of(hardyAct)).screenings(new ArrayList<>()).build();

            Film theGrandBudapest = Film.builder().title("The Grand Budapest Hotel").duration(99).year(2014)
                        .genre(comedy).nationality(german)
                        .imageUrl(imageBasePath + "inception.jpg")
                        .description("A concierge and his protégé become embroiled in a murder mystery.")
                        .director(andersonDir).actors(List.of(blanchettAct)).screenings(new ArrayList<>()).build();

            Film seven = Film.builder().title("Se7en").duration(127).year(1995).genre(thriller)
                        .nationality(american)
                        .imageUrl(imageBasePath + "inception.jpg")
                        .description("Two detectives hunt a serial killer who uses the seven deadly sins as motifs.")
                        .director(finchDir).actors(List.of(pittAct, washingtonAct)).screenings(new ArrayList<>())
                        .build();

            Film theIrishman = Film.builder().title("The Irishman").duration(209).year(2019).genre(crime)
                        .nationality(american)
                        .imageUrl(imageBasePath + "inception.jpg")
                        .description("Hitman Frank Sheeran looks back at the secrets he kept as a mob enforcer.")
                        .director(spielbergDir).actors(List.of(pittAct)).screenings(new ArrayList<>()).build();

            Film arrival = Film.builder().title("Arrival").duration(116).year(2016).genre(scienceFiction)
                        .nationality(american)
                        .imageUrl(imageBasePath + "inception.jpg")
                        .description("A linguist works with the military to communicate with alien lifeforms.")
                        .director(villeneuveDir).actors(List.of(damonAct)).screenings(new ArrayList<>()).build();

            filmRepository.saveAll(List.of(
                        inception, interstellar, dunkirk, gladiator, bladeRunner,
                        fightClub, theRevenant, schindlersList, castAway, theMartian,
                        trainspotting, theGrandBudapest, seven, theIrishman, arrival));

            // ----------------------------------------------------------------
            // 6 Halls
            // ----------------------------------------------------------------
            Hall hall1 = Hall.builder().number(1).capacity(120).screenings(new ArrayList<>()).build();
            Hall hall2 = Hall.builder().number(2).capacity(80).screenings(new ArrayList<>()).build();
            Hall hall3 = Hall.builder().number(3).capacity(200).screenings(new ArrayList<>()).build();
            Hall hall4 = Hall.builder().number(4).capacity(60).screenings(new ArrayList<>()).build();
            Hall hall5 = Hall.builder().number(5).capacity(150).screenings(new ArrayList<>()).build();
            Hall hall6 = Hall.builder().number(6).capacity(100).screenings(new ArrayList<>()).build();

            theaterRepository.saveAll(List.of(hall1, hall2, hall3, hall4, hall5, hall6));

            // ----------------------------------------------------------------
            // 15 Screenings (one per film, spread over halls)
            // ----------------------------------------------------------------
            Hall[] halls = { hall1, hall2, hall3, hall4, hall5, hall6 };
            Film[] films = {
                        inception, interstellar, dunkirk, gladiator, bladeRunner,
                        fightClub, theRevenant, schindlersList, castAway, theMartian,
                        trainspotting, theGrandBudapest, seven, theIrishman, arrival
            };

            List<Screening> screenings = new ArrayList<>();
            for (int i = 0; i < films.length; i++) {
                  LocalDateTime showTime = LocalDateTime.now();
                  Screening s = new Screening();
                  s.setFilm(films[i]);
                  s.setHall(halls[i % halls.length]);
                  s.setStartTime(showTime);
                  s.setEndTime(showTime.plusMinutes(films[i].getDuration()));
                  screenings.add(s);
            }
            screeningRepository.saveAll(screenings);

            // ----------------------------------------------------------------
            // Users: 1 admin + 10 regular users (11 total → >2 pages of 5)
            // ----------------------------------------------------------------
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setEmail("admin@cinema.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);

            for (int i = 1; i <= 10; i++) {
                  UserEntity u = new UserEntity();
                  u.setUsername("user" + i);
                  u.setEmail("user" + i + "@cinema.com");
                  u.setPassword(passwordEncoder.encode("password"));
                  u.setRole(Role.CUSTOMER);
                  userRepository.save(u);
            }

            // ----------------------------------------------------------------
            // Film Ratings (3 per film for the first 5 films)
            // ----------------------------------------------------------------
            Film[] ratedFilms = { inception, interstellar, dunkirk, gladiator, bladeRunner };
            int[] scores = { 5, 4, 3 };
            for (Film f : ratedFilms) {
                  for (int s = 0; s < scores.length; s++) {
                        FilmRating r = new FilmRating();
                        r.setCustomer(admin);
                        r.setFilm(f);
                        r.setScore(scores[s]);
                        filmRatingRepository.save(r);
                  }
            }

            // ----------------------------------------------------------------
            // Media (2 per film for first 5 films)
            // ----------------------------------------------------------------
            Film[] mediaFilms = { inception, interstellar, dunkirk, gladiator, bladeRunner };
            for (Film f : mediaFilms) {
                  Media poster = new Media();
                  poster.setFilm(f);
                  poster.setMediaType(MediaType.IMAGE);
                  poster.setMedia(imageBasePath + "inception.jpg");
                  mediaRepository.save(poster);

                  Media trailer = new Media();
                  trailer.setFilm(f);
                  trailer.setMediaType(MediaType.VIDEO);
                  trailer.setMedia("https://www.youtube.com/watch?v=example");
                  mediaRepository.save(trailer);
            }

            log.info("Data initialization completed successfully.");
      }
}
