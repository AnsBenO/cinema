package ntt.beca.films.screening;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ntt.beca.films.film.FilmDto;
import ntt.beca.films.hall.HallDto;
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
        PagedResultDto<ScreeningDto> screeningDtos = screeningService.getAllByDate(keyword, date, hallNumber,
                pageNumber);

        // Fetch halls for the dropdown
        List<HallDto> hallDtos = hallService.getAllNoPagination();

        // Add attributes for rendering
        model.addAttribute("halls", hallDtos);
        model.addAttribute("hallNumber", hallNumber);
        model.addAttribute("screenings", screeningDtos);
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
        List<HallDto> hallDtos = hallService.getAllNoPagination();
        List<FilmDto> filmDtos = filmService.getAllNoPagination();
        ScreeningDto screeningDto = new ScreeningDto();
        model.addAttribute("halls", hallDtos);
        model.addAttribute("films", filmDtos);
        model.addAttribute("screening", screeningDto);
        return "views/screenings/add-screening";
    }

    // Create new screening
    @PostMapping
    public String createScreening(@Valid @ModelAttribute("screening") ScreeningDto screeningDto,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<HallDto> hallDtos = hallService.getAllNoPagination();
            List<FilmDto> filmDtos = filmService.getAllNoPagination();
            model.addAttribute("halls", hallDtos);
            model.addAttribute("films", filmDtos);
            return "views/screenings/add-screening";
        }
        screeningService.save(screeningDto);
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
            List<HallDto> halls = hallService.getAllNoPagination();
            List<FilmDto> films = filmService.getAllNoPagination();
            ScreeningDto screeningDto = screeningService.getOne(id);
            model.addAttribute("halls", halls);
            model.addAttribute("films", films);
            model.addAttribute("screening", screeningDto);
            return "views/screenings/edit-screening";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Screening not found.");
            redirectAttributes.addFlashAttribute("status", false);
            return "views/screenings/edit-screening";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateScreening(@PathVariable Long id,
            @Valid @ModelAttribute("screening") ScreeningDto screeningDto,
            BindingResult bindingResult,
            Model model, RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            List<HallDto> halls = hallService.getAllNoPagination();
            List<FilmDto> films = filmService.getAllNoPagination();
            model.addAttribute("halls", halls);
            model.addAttribute("films", films);
            response.setStatus(422);
            return "views/screenings/edit-screening";
        }
        try {
            ScreeningDto existingScreening = screeningService.getOne(id);
            log.info("Before Update: {}", existingScreening);
            existingScreening.setFilm(screeningDto.getFilm());
            existingScreening.setStartTime(screeningDto.getStartTime());
            existingScreening.setEndTime(screeningDto.getEndTime());
            HallDto hallDto = screeningDto.getHall();
            existingScreening.setHall(hallDto);
            FilmDto filmDto = screeningDto.getFilm();
            existingScreening.setFilm(filmDto);
            log.info("After Update: {}", existingScreening);
            screeningService.save(existingScreening);
            if (request.getHeader("HX-Request") != null) {
                response.setHeader("HX-Redirect", "/screenings");
                return "views/screenings/edit-screening";
            }
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

        PagedResultDto<ScreeningDto> screeningDtos = screeningService.getAllByDate(null, null, pageNumber, pageNumber);

        model.addAttribute("screenings", screeningDtos);

        return "views/screenings/list-screenings :: screenings-table";

    }
}
