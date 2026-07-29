package com.ansbeno.films.genre;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/genres/search")
public class ApiGenreController {

      private final GenreService genreService;

      @GetMapping("/findAllNoPagination")
      public ResponseEntity<List<GenreDto>> findAllNoPagination() {
            return ResponseEntity.ok(genreService.getAllNoPagination());
      }
}
