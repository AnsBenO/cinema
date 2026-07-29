package com.ansbeno.films.screening;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/screenings/search")
public class ApiScreeningController {

      private final ScreeningService screeningService;

      @GetMapping("/findUpcomingScreenings")
      public ResponseEntity<List<ScreeningDto>> findUpcomingScreenings() {
            return ResponseEntity.ok(screeningService.getUpcomingScreenings());
      }
}
