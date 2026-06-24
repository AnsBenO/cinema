package ntt.beca.films.dashboard;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntt.beca.films.screening.ScreeningDto;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping
    public String getDashboard(
            Model model,
            HttpServletRequest request) {

        boolean isHtmxRequest = request.getHeader("HX-Request") != null;

        List<ScreeningDto> upcomingScreenings = dashboardService.getUpcomingScreenings();
        DashboardDataDto dashboardData = dashboardService.getDashboardData();

        model.addAttribute("screenings", upcomingScreenings);
        model.addAttribute("dashboardData", dashboardData);

        // If it's an HTMX request, return only the fragment
        if (isHtmxRequest) {
            return "views/dashboard/dashboard :: dashboardContent";
        }

        // Otherwise, return the full dashboard page
        return "views/dashboard/dashboard";
    }
}
