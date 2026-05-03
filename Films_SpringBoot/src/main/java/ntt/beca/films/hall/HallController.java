package ntt.beca.films.hall;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ntt.beca.films.shared.service.PagedResultDto;

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

	//
	@GetMapping("/add")
	public String showAddHall(Model model) {
		HallDto hallDto = new HallDto();
		model.addAttribute("hall", hallDto);

		return "views/halls/add-hall";
	}

	@PostMapping("/save")
	public String createHall(@ModelAttribute HallDto hallDto) {
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
		return "redirect:/halls";
	}

	@GetMapping("/delete/{id}")
	public String deleteHall(@PathVariable Long id, Model model) {

		hallService.delete(id);
		return "redirect:/halls";

	}
}