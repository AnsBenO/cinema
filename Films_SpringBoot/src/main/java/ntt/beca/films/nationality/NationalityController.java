package ntt.beca.films.nationality;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import ntt.beca.films.shared.service.PagedResultDto;

@Controller
@RequestMapping("/nationalities")
public class NationalityController {

    private NationalityService nationalityService;

    public NationalityController(NationalityService nationalityService) {
        this.nationalityService = nationalityService;
    }

    @GetMapping("")
    public String getAllNationalities(@RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "") String keyword,
            HttpServletRequest request,
            Model model) {
        PagedResultDto<Nationality> nationalities = nationalityService.getAll(page, keyword);
        model.addAttribute("nationalities", nationalities);
        model.addAttribute("keyword", keyword);

        boolean isHtmxRequest = request.getHeader("HX-Request") != null;

        if (isHtmxRequest) {
            return "views/nationalities/list-nationalities::nationalities-table";
        }
        return "views/nationalities/list-nationalities";
    }

    @GetMapping("/add")
    public String showAddNationalityForm(Model model) {
        Nationality nationality = new Nationality();
        model.addAttribute("nationality", nationality);
        return "views/nationalities/add-nationality";
    }

    @GetMapping("/edit/{id}")
    public String getNationalityById(@PathVariable Long id, Model model) {
        Nationality nationality = nationalityService.getOne(id);
        model.addAttribute("nationality", nationality);

        return "views/nationalities/edit-nationality";
    }

    @PostMapping("/save")
    public String createNationality(@ModelAttribute Nationality nationality, RedirectAttributes redirectAttributes) {

        if (nationality.getId() != null) {
            Nationality existingNationality = nationalityService.getOne(nationality.getId());
            if (existingNationality != null) {

                existingNationality.setLabel(nationality.getLabel());
                nationalityService.save(existingNationality);
            }
        } else {
            nationalityService.save(nationality);
        }

        redirectAttributes.addFlashAttribute("message", "Nationality created successfully!");
        redirectAttributes.addFlashAttribute("status", true);
        return "redirect:/nationalities";
    }

    @GetMapping("/delete/{id}")
    public String deleteNationality(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (nationalityService.delete(id)) {
            redirectAttributes.addFlashAttribute("message", "Nationality deleted successfully!");
            redirectAttributes.addFlashAttribute("status", true);
        } else {
            redirectAttributes.addFlashAttribute("message", "Failed to delete nationality.");
            redirectAttributes.addFlashAttribute("status", false);
        }
        return "redirect:/nationalities";
    }

}
