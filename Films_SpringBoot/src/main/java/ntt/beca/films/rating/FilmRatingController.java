package ntt.beca.films.rating;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import ntt.beca.films.shared.service.PagedResultDto;

@Controller
@RequestMapping("/film-ratings")
public class FilmRatingController {
	private FilmRatingService filmRatingService;

	public FilmRatingController(FilmRatingService filmRatingService) {
		this.filmRatingService = filmRatingService;
	}

	@GetMapping("")
	public String getAllFilmRatings(@RequestParam(defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "") String keyword,
			@RequestParam(required = false, defaultValue = "") String genre,
			HttpServletRequest request,
			Model model) {
		PagedResultDto<FilmRating> filmRatings = filmRatingService.getAll(page, keyword, genre);

		model.addAttribute("filmRatings", filmRatings);
		model.addAttribute("keyword", keyword);
		model.addAttribute("genre", genre);

		boolean isHtmxRequest = request.getHeader("HX-Request") != null;

		if (isHtmxRequest) {
			return "views/film-ratings/list-film-ratings :: ratings-table";
		}
		return "views/film-ratings/list-film-ratings";
	}
}
