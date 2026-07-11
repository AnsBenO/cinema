package com.ansbeno.films.rating;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ansbeno.films.shared.service.PagedResultDto;

import jakarta.servlet.http.HttpServletRequest;

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
			HttpServletRequest request,
			Model model) {
		PagedResultDto<RatingDto> filmRatings = filmRatingService.getAll(page, keyword);

		model.addAttribute("filmRatings", filmRatings);
		model.addAttribute("keyword", keyword);

		boolean isHtmxRequest = request.getHeader("HX-Request") != null;

		if (isHtmxRequest) {
			return "views/film-ratings/list-film-ratings :: film-ratings-table";
		}
		return "views/film-ratings/list-film-ratings";
	}
}
