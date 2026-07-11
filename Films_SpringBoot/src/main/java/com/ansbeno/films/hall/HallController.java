package com.ansbeno.films.hall;

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
@RequestMapping("/halls")
public class HallController {

	private final HallService hallService;

	@GetMapping("")
	public String getAllHalls(@RequestParam(defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "") String keyword,
			HttpServletRequest request, Model model) {

		PagedResultDto<HallDto> hallDtos = hallService.getAll(page, keyword);
		model.addAttribute("halls", hallDtos);
		model.addAttribute("keyword", keyword);
		boolean isHtmxRequest = request.getHeader("HX-Request") != null;
		// Return partial HTML if it's an HTMX request
		if (isHtmxRequest) {
			return "views/halls/list-halls::halls-table";
		}
		return "views/halls/list-halls";
	}

	@GetMapping("/edit/{id}")
	public String getHallById(@PathVariable Long id, Model model) {
		HallDto hall = hallService.getOne(id);
		model.addAttribute("hall", hall);

		return "views/halls/edit-hall";
	}

	@PostMapping("/edit/{id}")
	public String updateHall(@PathVariable Long id, @Valid @ModelAttribute("hall") HallDto hallDto,
			BindingResult bindingResult, RedirectAttributes redirectAttributes,
			HttpServletRequest request, HttpServletResponse response) {
		if (bindingResult.hasErrors()) {
			response.setStatus(422);
			return "views/halls/edit-hall";
		}
		try {
			HallDto existingHall = hallService.getOne(id);
			existingHall.setNumber(hallDto.getNumber());
			existingHall.setCapacity(hallDto.getCapacity());
			hallService.save(existingHall);

			if (request.getHeader("HX-Request") != null) {
				response.setHeader("HX-Redirect", "/halls");
				return "views/halls/edit-hall";
			}
			redirectAttributes.addFlashAttribute("message", "hall updated successfully!");
			redirectAttributes.addFlashAttribute("status", true);
		} catch (Exception e) {
			if (request.getHeader("HX-Request") != null) {
				response.setHeader("HX-Redirect", "/halls");
				return "views/halls/edit-hall";
			}
			redirectAttributes.addFlashAttribute("message", "Failed to update hall.");
			redirectAttributes.addFlashAttribute("status", false);
		}
		return "redirect:/halls";
	}

	//
	@GetMapping("/add")
	public String showAddHall(Model model) {
		HallDto hallDto = new HallDto();
		model.addAttribute("hall", hallDto);

		return "views/halls/add-hall";
	}

	@PostMapping("/save")
	public String createHall(@Valid @ModelAttribute("hall") HallDto hallDto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
		if (bindingResult.hasErrors()) {
			response.setStatus(422);
			return hallDto.getId() != null ? "views/halls/edit-hall" : "views/halls/add-hall";
		}
		if (hallDto.getId() != null) {
			HallDto existingHall = hallService.getOne(hallDto.getId());
			if (existingHall != null) {
				existingHall.setNumber(hallDto.getNumber());
				existingHall.setCapacity(hallDto.getCapacity());
				hallService.save(existingHall);
			}
		} else {
			hallService.save(hallDto);
		}
		if (request.getHeader("HX-Request") != null) {
			response.setHeader("HX-Redirect", "/halls");
			return hallDto.getId() != null ? "views/halls/edit-hall" : "views/halls/add-hall";
		}
		redirectAttributes.addFlashAttribute("message", "Hall saved successfully!");
		redirectAttributes.addFlashAttribute("status", true);
		return "redirect:/halls";
	}

	@GetMapping("/delete/{id}")
	public String deleteHall(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		hallService.delete(id);
		redirectAttributes.addFlashAttribute("message", "Hall deleted successfully!");
		redirectAttributes.addFlashAttribute("status", true);
		return "redirect:/halls";
	}

}
