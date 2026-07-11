package com.ansbeno.films.nationality;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ansbeno.films.shared.service.PagedResultDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/nationalities")
public class NationalityController {

    private final NationalityService nationalityService;

    @GetMapping("")
    public String getAllNationalities(@RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "") String keyword,
            HttpServletRequest request,
            Model model) {
        PagedResultDto<NationalityDto> nationalityDtos = nationalityService.getAll(page, keyword);
        model.addAttribute("nationalities", nationalityDtos);
        model.addAttribute("keyword", keyword);

        boolean isHtmxRequest = request.getHeader("HX-Request") != null;

        if (isHtmxRequest) {
            return "views/nationalities/list-nationalities::nationalities-table";
        }
        return "views/nationalities/list-nationalities";
    }

    @GetMapping("/add")
    public String showAddNationalityForm(Model model) {
        NationalityDto nationalityDto = new NationalityDto();
        model.addAttribute("nationality", nationalityDto);
        return "views/nationalities/add-nationality";
    }

    @GetMapping("/edit/{id}")
    public String getNationalityById(@PathVariable Long id, Model model) {
        NationalityDto nationalityDto = nationalityService.getOne(id);
        model.addAttribute("nationality", nationalityDto);

        return "views/nationalities/edit-nationality";
    }

    @PostMapping("/save")
    public String createNationality(@Valid @ModelAttribute("nationality") NationalityDto nationalityDto,
            BindingResult bindingResult, RedirectAttributes redirectAttributes,
            HttpServletRequest request, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            response.setStatus(422);
            return nationalityDto.getId() != null
                    ? "views/nationalities/edit-nationality"
                    : "views/nationalities/add-nationality";
        }
        nationalityService.save(nationalityDto);
        if (request.getHeader("HX-Request") != null) {
            response.setHeader("HX-Redirect", "/nationalities");
            return nationalityDto.getId() != null
                    ? "views/nationalities/edit-nationality"
                    : "views/nationalities/add-nationality";
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
