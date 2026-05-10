package ntt.beca.films.film;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ntt.beca.films.genre.GenreDto;
import ntt.beca.films.nationality.NationalityDto;
import ntt.beca.films.person.PersonDto;
import ntt.beca.films.genre.GenreService;
import ntt.beca.films.nationality.NationalityService;
import ntt.beca.films.shared.service.PagedResultDto;
import ntt.beca.films.person.PersonService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final GenreService genreService;
    private final NationalityService nationalityService;
    private final PersonService personService;

    @GetMapping("")
    public String getAllFilms(@RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "") String genre,
            HttpServletRequest request,
            Model model) {
        PagedResultDto<FilmDto> filmDtos = filmService.getAll(page, keyword, genre);
        model.addAttribute("films", filmDtos);
        model.addAttribute("keyword", keyword);
        model.addAttribute("genre", genre);
        List<GenreDto> genreDtos = genreService.getAllNoPagination();
        model.addAttribute("genres", genreDtos);
        return request.getHeader("HX-Request") != null ? "views/films/list-films :: films-table"
                : "views/films/list-films";
    }

    @GetMapping("/add")
    public String showAddFilmForm(Model model) {
        model.addAttribute("film", new FilmDto());
        List<GenreDto> genreDtos = genreService.getAllNoPagination();
        List<NationalityDto> nationalityDtos = nationalityService.getAllNoPagination();
        List<PersonDto> actors = personService.getAllActorsNoPagination();
        List<PersonDto> directors = personService.getAllDirectorsNoPagination();
        model.addAttribute("directors", directors);
        model.addAttribute("genres", genreDtos);
        model.addAttribute("actors", actors);
        model.addAttribute("nationalities", nationalityDtos);
        return "views/films/add-film";
    }

    @PostMapping("")
    public String createFilm(@RequestParam(value = "file", required = false) MultipartFile file,
            @Valid @ModelAttribute("film") FilmDto filmDto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("genres", genreService.getAllNoPagination());
            model.addAttribute("nationalities", nationalityService.getAllNoPagination());
            model.addAttribute("actors", personService.getAllActorsNoPagination());
            model.addAttribute("directors", personService.getAllDirectorsNoPagination());
            return "views/films/add-film";
        }
        try {
            if (file != null && !file.isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
                        + File.separator + "resources" + File.separator + "static" + File.separator + "images";

                String fileName = file.getOriginalFilename();

                File uploadDirFile = new File(uploadDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }

                File dest = new File(uploadDir + File.separator + fileName);
                file.transferTo(dest);

                filmDto.setImageUrl("/images/" + fileName);
            }

            filmService.save(filmDto);
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
            FilmDto filmDto = filmService.getOne(id);

            model.addAttribute("film", filmDto);
            model.addAttribute("nationalities", nationalityService.getAllNoPagination());
            model.addAttribute("genres", genreService.getAllNoPagination());
            model.addAttribute("directors", personService.getAllDirectorsNoPagination());
            model.addAttribute("actors", personService.getAllActorsNoPagination());

            model.addAttribute("film", filmDto);
            return "views/films/edit-film";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Film not found: " + e.getMessage());
            redirectAttributes.addFlashAttribute("status", false);
            return "redirect:/films";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateFilm(@PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @Valid @ModelAttribute("film") FilmDto filmDto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("nationalities", nationalityService.getAllNoPagination());
            model.addAttribute("genres", genreService.getAllNoPagination());
            model.addAttribute("directors", personService.getAllDirectorsNoPagination());
            model.addAttribute("actors", personService.getAllActorsNoPagination());
            response.setStatus(422);
            return "views/films/edit-film";
        }
        try {
            FilmDto existingFilm = filmService.getOne(id);

            // Update film details
            existingFilm.setTitle(filmDto.getTitle());
            existingFilm.setGenre(filmDto.getGenre());
            existingFilm.setDuration(filmDto.getDuration());
            existingFilm.setYear(filmDto.getYear());
            existingFilm.setNationality(filmDto.getNationality());
            existingFilm.setDirector(filmDto.getDirector());
            existingFilm.setActors(filmDto.getActors());

            if (file != null && !file.isEmpty()) {
                String oldImageUrl = existingFilm.getImageUrl();
                if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                    File oldFile = new File(
                            System.getProperty("user.dir") + "/src/main/resources/static" + oldImageUrl);
                    if (oldFile.exists()) {
                        boolean deleted = Files.deleteIfExists(oldFile.toPath());
                        if (!deleted) {
                            throw new IOException("Failed to delete old image file: " + oldFile.getAbsolutePath());
                        }
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
            if (request.getHeader("HX-Request") != null) {
                response.setHeader("HX-Redirect", "/films");
                return "views/films/edit-film";
            }
            redirectAttributes.addFlashAttribute("message", "Film updated successfully!");
            redirectAttributes.addFlashAttribute("status", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to update film: " + e.getMessage());
            redirectAttributes.addFlashAttribute("status", false);
        }
        return "redirect:/films";
    }
}
