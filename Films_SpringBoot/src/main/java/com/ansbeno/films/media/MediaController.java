package com.ansbeno.films.media;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ansbeno.films.film.FilmDto;
import com.ansbeno.films.film.FilmService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/medias")
public class MediaController {
    private final MediaService mediaService;
    private final FilmService filmService;

    @GetMapping("/{filmId}")
    public String showMediaOfFilms(@PathVariable Long filmId,
            HttpServletRequest request,
            Model model) {
        FilmDto filmDto = filmService.getOne(filmId);
        List<MediaDto> mediaDtos = mediaService.findMediaOfFilms(filmId);
        model.addAttribute("film", filmDto);
        model.addAttribute("medias", mediaDtos);

        return "views/media/show-medias";
    }

    @GetMapping("/add")
    public String showAddMediaForm(Model model) {
        MediaDto mediaDto = new MediaDto();
        model.addAttribute("media", mediaDto);
        return "views/media/add-media";
    }

    @PostMapping("/save")
    public String createMedia(@RequestParam("file") MultipartFile file,
            @ModelAttribute MediaDto media) {
        try {
            mediaService.save(media);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/medias/1";
        }
        return "redirect:/medias/1";
    }

}
