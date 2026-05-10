package ntt.beca.films.person;

import java.util.Arrays;
import java.util.List;

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
import ntt.beca.films.nationality.NationalityDto;
import ntt.beca.films.nationality.NationalityService;
import ntt.beca.films.shared.service.PagedResultDto;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;
    private final NationalityService nationalityService;

    @GetMapping
    public String getAllPersons(@RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "") String genre,
            @RequestParam(required = false, defaultValue = "") String personType,
            HttpServletRequest request,
            Model model) {
        PagedResultDto<PersonDto> personDtos = personService.getAll(page, keyword, personType);
        List<String> types = Arrays.stream(PersonType.values()).map(Enum::name).toList();
        model.addAttribute("persons", personDtos);
        model.addAttribute("keyword", keyword);
        model.addAttribute("genre", genre);
        model.addAttribute("personType", personType);
        model.addAttribute("types", types);
        return request.getHeader("HX-Request") != null ? "views/persons/list-persons :: personsTable"
                : "views/persons/list-persons";
    }

    @GetMapping("/add")
    public String showAddPersonForm(Model model) {
        List<String> types = Arrays.stream(PersonType.values()).map(Enum::name).toList();
        List<NationalityDto> nationalityDtos = nationalityService.getAllNoPagination();

        model.addAttribute("person", new PersonDto());
        model.addAttribute("nationalities", nationalityDtos);
        model.addAttribute("types", types);
        return "views/persons/add-person";
    }

    @PostMapping
    public String createPerson(@Valid @ModelAttribute("person") PersonDto person, BindingResult bindingResult,
            Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            List<String> types = Arrays.stream(PersonType.values()).map(Enum::name).toList();
            List<NationalityDto> nationalityDtos = nationalityService.getAllNoPagination();
            model.addAttribute("nationalities", nationalityDtos);
            model.addAttribute("types", types);
            return "views/persons/add-person";
        }
        personService.save(person);
        redirectAttributes.addFlashAttribute("message", "Person added successfully!");
        redirectAttributes.addFlashAttribute("status", true);
        return "redirect:/persons";
    }

    @GetMapping("/delete/{id}")
    public String deletePerson(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        log.info("deleting person with id {}", id);

        personService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Person deleted successfully!");
        redirectAttributes.addFlashAttribute("status", true);
        return "redirect:/persons";
    }

    @GetMapping("/edit/{id}")
    public String showEditPersonForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            PersonDto personDto = personService.getOne(id);
            List<String> types = Arrays.stream(PersonType.values()).map(Enum::name).toList();
            List<NationalityDto> nationalityDtos = nationalityService.getAllNoPagination();

            model.addAttribute("nationalities", nationalityDtos);
            model.addAttribute("types", types);
            model.addAttribute("person", personDto);
            return "views/persons/edit-person";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Person not found: " + e.getMessage());
            redirectAttributes.addFlashAttribute("status", false);
            return "redirect:/persons";
        }
    }

    @PostMapping("/edit/{id}")
    public String updatePerson(@PathVariable Long id,
            @Valid @ModelAttribute("person") PersonDto person,
            BindingResult bindingResult,
            Model model, RedirectAttributes redirectAttributes,
            HttpServletRequest request, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            List<String> types = Arrays.stream(PersonType.values()).map(Enum::name).toList();
            List<NationalityDto> nationalityDtos = nationalityService.getAllNoPagination();
            model.addAttribute("nationalities", nationalityDtos);
            model.addAttribute("types", types);
            response.setStatus(422);
            return "views/persons/edit-person";
        }
        try {
            PersonDto existingPerson = personService.getOne(id);
            existingPerson.setFirstName(person.getFirstName());
            existingPerson.setLastName(person.getLastName());
            existingPerson.setPhoto(person.getPhoto());
            existingPerson.setBirthDate(person.getBirthDate());
            existingPerson.setPersonType(person.getPersonType());
            existingPerson.setNationality(person.getNationality());
            personService.save(existingPerson);
            if (request.getHeader("HX-Request") != null) {
                response.setHeader("HX-Redirect", "/persons");
                return "views/persons/edit-person";
            }
            redirectAttributes.addFlashAttribute("message", "Person updated successfully!");
            redirectAttributes.addFlashAttribute("status", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to update person: " + e.getMessage());
            redirectAttributes.addFlashAttribute("status", false);
        }
        return "redirect:/persons";
    }

    @PostMapping("/save")
    public String savePerson(@Valid @ModelAttribute("person") PersonDto person,
            BindingResult bindingResult,
            Model model, RedirectAttributes redirectAttributes,
            HttpServletRequest request, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            List<String> types = Arrays.stream(PersonType.values()).map(Enum::name).toList();
            List<NationalityDto> nationalityDtos = nationalityService.getAllNoPagination();
            model.addAttribute("nationalities", nationalityDtos);
            model.addAttribute("types", types);
            response.setStatus(422);
            return person.getId() != null ? "views/persons/edit-person" : "views/persons/add-person";
        }
        if (person.getId() != null) {
            try {
                PersonDto existingPerson = personService.getOne(person.getId());
                existingPerson.setFirstName(person.getFirstName());
                existingPerson.setLastName(person.getLastName());
                existingPerson.setPhoto(person.getPhoto());
                existingPerson.setBirthDate(person.getBirthDate());
                existingPerson.setPersonType(person.getPersonType());
                existingPerson.setNationality(person.getNationality());
                personService.save(existingPerson);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("message", "Failed to update person: " + e.getMessage());
                redirectAttributes.addFlashAttribute("status", false);
                return "redirect:/persons";
            }
        } else {
            personService.save(person);
        }
        if (request.getHeader("HX-Request") != null) {
            response.setHeader("HX-Redirect", "/persons");
            return person.getId() != null ? "views/persons/edit-person" : "views/persons/add-person";
        }
        redirectAttributes.addFlashAttribute("message", "Person saved successfully!");
        redirectAttributes.addFlashAttribute("status", true);
        return "redirect:/persons";
    }
}
