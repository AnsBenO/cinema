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
import ntt.beca.films.shared.service.PagedResultDto;

@Controller
@RequestMapping("/halls")
public class HallController {

	private HallService hallService;

	public void setCategorieSeviceImpl(HallService hallService) {
		this.hallService = hallService;
	}

	public HallController(HallService hallService) {
		this.hallService = hallService;
	}

	@GetMapping("")
	public String getAllHalls(@RequestParam(defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "") String keyword,
			HttpServletRequest request, Model model) {

		PagedResultDto<Hall> halls = hallService.getAll(page, keyword);
		model.addAttribute("halls", halls);
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
		Hall hall = hallService.getOne(id);
		model.addAttribute("hall", hall);

		return "views/halls/edit-hall";
	}

	//
	@GetMapping("/add")
	public String ashowAddHall(Model model) {
		Hall hall = new Hall();
		model.addAttribute("hall", hall);

		return "views/halls/add-hall";
	}

	@PostMapping("/save")
	public String createHall(@ModelAttribute Hall hall) {
		if (hall.getId() != null) {
			Hall existingHall = hallService.getOne(hall.getId());
			if (existingHall != null) {

				existingHall.setNumber(hall.getNumber());
				existingHall.setCapacity(hall.getCapacity());
				hallService.save(existingHall);
			}
		} else {
			hallService.save(hall);
		}
		return "redirect:/halls";
	}

	@GetMapping("/delete/{id}")
	public String deletHall(@PathVariable Long id, Model model) {

		hallService.delete(id);
		return "redirect:/halls";

	}
}