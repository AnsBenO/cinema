package ntt.beca.films.film;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

import ntt.beca.films.genre.Genre;
import ntt.beca.films.nationality.Nationality;
import ntt.beca.films.person.Person;
import ntt.beca.films.genre.GenreService;
import ntt.beca.films.nationality.NationalityService;
import ntt.beca.films.shared.service.PagedResultDto;
import ntt.beca.films.person.PersonService;

@Controller
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final GenreService genreService;
    private final NationalityService nationalityService;
    private PersonService personService;

    public FilmController(FilmService filmService, GenreService genreService, NationalityService nationalityService,
            PersonService personService) {
        this.filmService = filmService;
        this.genreService = genreService;
        this.nationalityService = nationalityService;
        this.personService = personService;
    }

    @GetMapping("")
    public String getAllFilms(@RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "") String genre,
            HttpServletRequest request,
            Model model) {
        PagedResultDto<Film> films = filmService.getAll(page, keyword, genre);
        model.addAttribute("films", films);
        model.addAttribute("keyword", keyword);
        model.addAttribute("genre", genre);
        List<Genre> genres = genreService.getAllNoPagination();
        model.addAttribute("genres", genres);
        return request.getHeader("HX-Request") != null ? "views/films/list-films :: films-table"
                : "views/films/list-films";
    }

    @GetMapping("/add")
    public String showAddFilmForm(Model model) {
        model.addAttribute("film", new Film());
        List<Genre> genres = genreService.getAllNoPagination();
        List<Nationality> nationalities = nationalityService.getAllNoPagination();
        List<Person> actors = personService.getAllActorsNoPagination();
        List<Person> directors = personService.getAllDirectorsNoPagination();
        model.addAttribute("directors", directors);
        model.addAttribute("genres", genres);
        model.addAttribute("actors", actors);
        model.addAttribute("nationalities", nationalities);
        return "views/films/add-film";
    }

    @PostMapping("")
    public String createFilm(@RequestParam("file") MultipartFile file, @ModelAttribute Film film,
            RedirectAttributes redirectAttributes) {
        try {
            if (file.isEmpty()) {
                System.out.println("empty");
            } else {
                String uploadDir = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
                        + File.separator + "resources" + File.separator + "static" + File.separator + "images";

                String fileName = file.getOriginalFilename();

                File uploadDirFile = new File(uploadDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }

                File dest = new File(uploadDir + File.separator + fileName);
                file.transferTo(dest);

                film.setImageUrl("/images/" + fileName);
            }

            filmService.save(film);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/films";
        }
        return "redirect:/films";
    }

    @GetMapping("/delete/{id}")
    public String deleteFilm(@PathVariable Long id) {
        filmService.delete(id);
        return "redirect:/films";
    }

    @GetMapping("/edit/{id}")
    public String showEditFilmForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Film film = filmService.getOne(id);

            model.addAttribute("film", film);
            model.addAttribute("nationalities", nationalityService.getAllNoPagination());
            model.addAttribute("genres", genreService.getAllNoPagination());
            model.addAttribute("directors", personService.getAllDirectorsNoPagination());
            model.addAttribute("actors", personService.getAllActorsNoPagination());

            model.addAttribute("film", film);
            return "views/films/edit-film";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Film not found: " + e.getMessage());
            redirectAttributes.addFlashAttribute("status", false);
            return "redirect:/films";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateFilm(@PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @ModelAttribute Film film,
            RedirectAttributes redirectAttributes) {
        try {
            Film existingFilm = filmService.getOne(id);

            // Update film details
            existingFilm.setTitle(film.getTitle());
            existingFilm.setGenre(film.getGenre());
            existingFilm.setDuration(film.getDuration());
            existingFilm.setYear(film.getYear());
            existingFilm.setNationality(film.getNationality());
            existingFilm.setDirector(film.getDirector());
            existingFilm.setActors(film.getActors());

            if (!file.isEmpty()) {
                String oldImageUrl = existingFilm.getImageUrl();
                if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                    File oldFile = new File(
                            System.getProperty("user.dir") + "/src/main/resources/static" + oldImageUrl);
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }

                String uploadDir = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
                        + File.separator + "resources" + File.separator + "static" + File.separator + "images";

                String fileName = file.getOriginalFilename();
                File uploadDirFile = new File(uploadDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }

                File dest = new File(uploadDir + File.separator + fileName);
                file.transferTo(dest);

                existingFilm.setImageUrl("/images/" + fileName);
            }

            // Save the updated film
            filmService.save(existingFilm);
            redirectAttributes.addFlashAttribute("message", "Film updated successfully!");
            redirectAttributes.addFlashAttribute("status", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to update film: " + e.getMessage());
            redirectAttributes.addFlashAttribute("status", false);
        }
        return "redirect:/films";
    }
}
