package ntt.beca.films.screening;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ntt.beca.films.film.Film;
import ntt.beca.films.hall.Hall;
import ntt.beca.films.film.FilmService;
import ntt.beca.films.hall.HallService;
import ntt.beca.films.shared.service.PagedResultDto;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/screenings")
public class ScreeningController {

    private final ScreeningService screeningService;
    private final FilmService filmService;
    private final HallService hallService;

    @GetMapping
    public String getAllScreenings(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer hallNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpServletRequest request,
            Model model) {

        int pageNumber = Math.max(0, page - 1); // Convert to 0-based index

        // Fetch paginated screenings
        PagedResultDto<Screening> screenings = screeningService.getAllByDate(keyword, date, hallNumber, pageNumber);

        // Fetch halls for the dropdown
        List<Hall> halls = hallService.getAllNoPagination();

        // Add attributes for rendering
        model.addAttribute("halls", halls);
        model.addAttribute("hallNumber", hallNumber);
        model.addAttribute("screenings", screenings);
        model.addAttribute("keyword", keyword);
        model.addAttribute("date", date);

        // Check if the request is an HTMX request
        boolean isHtmxRequest = request.getHeader("HX-Request") != null;

        if (isHtmxRequest) {
            return "views/screenings/list-screenings :: screenings-table";
        }

        return "views/screenings/list-screenings";
    }

    // Show form to add new screening
    @GetMapping("/add")
    public String showAddScreeningForm(Model model) {
        List<Hall> halls = hallService.getAllNoPagination();
        List<Film> films = filmService.getAllNoPagination();
        Screening screening = new Screening();
        model.addAttribute("halls", halls);
        model.addAttribute("films", films);
        model.addAttribute("screening", screening);
        return "views/screenings/add-screening";
    }

    // Create new screening
    @PostMapping
    public String createScreening(@ModelAttribute Screening screening) {
        screeningService.save(screening);
        return "redirect:/screenings";
    }

    // Delete screening by ID
    @GetMapping("/delete/{id}")
    public String deleteScreening(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (screeningService.delete(id)) {
            redirectAttributes.addFlashAttribute("message", "Screening deleted successfully!");
            redirectAttributes.addFlashAttribute("status", true);
        } else {
            redirectAttributes.addFlashAttribute("message", "Failed to delete screening.");
            redirectAttributes.addFlashAttribute("status", false);
        }
        return "redirect:/screenings";
    }

    // Show form to edit screening by ID
    @GetMapping("/edit/{id}")
    public String showEditScreeningForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            List<Hall> halls = hallService.getAllNoPagination();
            List<Film> films = filmService.getAllNoPagination();
            Screening screening = screeningService.getOne(id);
            model.addAttribute("halls", halls);
            model.addAttribute("films", films);
            model.addAttribute("screening", screening);
            return "views/screenings/edit-screening";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Screening not found.");
            redirectAttributes.addFlashAttribute("status", false);
            return "views/screenings/edit-screening";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateScreening(@PathVariable Long id,
            @ModelAttribute Screening screening,
            RedirectAttributes redirectAttributes) {
        try {
            Screening existingScreening = screeningService.getOne(id);
            log.info("Before Update: {}", existingScreening);
            existingScreening.setFilm(screening.getFilm());
            existingScreening.setStartTime(screening.getStartTime());
            existingScreening.setEndTime(screening.getEndTime());
            Hall hall = screening.getHall();
            existingScreening.setHall(hall);
            Film film = screening.getFilm();
            existingScreening.setFilm(film);
            log.info("After Update: {}", existingScreening);
            screeningService.save(existingScreening);
            redirectAttributes.addFlashAttribute("message", "Screening updated successfully!");
            redirectAttributes.addFlashAttribute("status", true);
        } catch (Exception e) {
            log.error("Update failed", e);
            redirectAttributes.addFlashAttribute("message", "Failed to update screening.");
            redirectAttributes.addFlashAttribute("status", false);
        }
        return "redirect:/screenings";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteScreening(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            HttpServletRequest request,
            Model model) {

        screeningService.delete(id); // Perform the deletion

        // Re-fetch the screenings for the current page
        int pageNumber = Math.max(0, page - 1); // Convert to 0-based index

        PagedResultDto<Screening> screenings = screeningService.getAllByDate(null, null, pageNumber, pageNumber);

        model.addAttribute("screenings", screenings);

        return "views/screenings/list-screenings :: screenings-table";

    }
}
