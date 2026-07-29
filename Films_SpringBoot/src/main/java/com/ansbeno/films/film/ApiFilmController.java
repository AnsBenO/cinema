package com.ansbeno.films.film;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ansbeno.films.shared.service.PagedResultDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/films/search")
public class ApiFilmController {

      private final FilmService filmService;

      @GetMapping("/findByTitleAndGenre")
      public ResponseEntity<PagedResultDto<FilmDto>> findByTitleAndGenre(
                  @RequestParam(defaultValue = "") String keyword,
                  @RequestParam(name = "genreLabel", defaultValue = "") String genreLabel,
                  @RequestParam(defaultValue = "1") int page) {
            return ResponseEntity.ok(filmService.getAll(page, keyword, genreLabel));
      }
}
