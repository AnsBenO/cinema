package ntt.beca.films.genre;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

import ntt.beca.films.shared.service.PagedResultDto;

@Controller
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    // Get all genres with pagination and search
    @GetMapping("")
    public String getAllGenres(@RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "") String genre,
            HttpServletRequest request,
            Model model) {
        PagedResultDto<Genre> genres = genreService.getAll(page, keyword, genre);

        model.addAttribute("genres", genres);
        model.addAttribute("keyword", keyword);
        model.addAttribute("genre", genre);

        boolean isHtmxRequest = request.getHeader("HX-Request") != null;

        if (isHtmxRequest) {
            return "views/genres/list-genres :: genre-table";
        }

        return "views/genres/list-genres"; // Path to the HTML view
    }

    // Show form to add new genre
    @GetMapping("/add")
    public String showAddGenreForm(Model model) {
        Genre genre = new Genre();
        model.addAttribute("genre", genre);
        return "views/genres/add-genre"; // Path to the HTML form for adding genre
    }

    // Create new genre
    @PostMapping
    public String createGenre(@ModelAttribute Genre genre, RedirectAttributes redirectAttributes) {
        genreService.save(genre);
        redirectAttributes.addFlashAttribute("message", "Genre added successfully!");
        redirectAttributes.addFlashAttribute("status", true);
        return "redirect:/genres";
    }

    // Delete genre by ID
    @GetMapping("/delete/{id}")
    public String deleteGenre(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (genreService.delete(id)) {
            redirectAttributes.addFlashAttribute("message", "Genre deleted successfully!");
            redirectAttributes.addFlashAttribute("status", true);
        } else {
            redirectAttributes.addFlashAttribute("message", "Failed to delete genre.");
            redirectAttributes.addFlashAttribute("status", false);
        }
        return "redirect:/genres";
    }

    // Show form to edit genre by ID
    @GetMapping("/edit/{id}")
    public String showEditGenreForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Genre genre = genreService.getOne(id);
            model.addAttribute("genre", genre);
            return "views/genres/edit-genre"; // Path to the HTML form for editing genre
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Genre not found.");
            redirectAttributes.addFlashAttribute("status", false);
            return "redirect:/genres";
        }
    }

    // Update genre information
    @PostMapping("/edit/{id}")
    public String updateGenre(@PathVariable Long id, @ModelAttribute Genre genre,
            RedirectAttributes redirectAttributes) {
        try {
            Genre existingGenre = genreService.getOne(id);
            existingGenre.setLabel(genre.getLabel());
            genreService.save(existingGenre);
            redirectAttributes.addFlashAttribute("message", "Genre updated successfully!");
            redirectAttributes.addFlashAttribute("status", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to update genre.");
            redirectAttributes.addFlashAttribute("status", false);
        }
        return "redirect:/genres";
    }
}
