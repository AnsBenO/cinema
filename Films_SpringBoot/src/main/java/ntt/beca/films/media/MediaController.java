package ntt.beca.films.media;

import java.io.File;
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

import jakarta.servlet.http.HttpServletRequest;
import ntt.beca.films.film.Film;
import ntt.beca.films.film.FilmService;

@Controller
@RequestMapping("/medias")
public class MediaController {
    private MediaService mediaService;
    private FilmService filmService;

    public MediaController(MediaService mediaService, FilmService filmService) {
        this.mediaService = mediaService;
        this.filmService = filmService;
    }

    @GetMapping("/{id_film}")
    public String showMediaOfFilms(@PathVariable Long id_film,
            HttpServletRequest request,
            Model model) {
        Film film = new Film();
        film = filmService.getOne(id_film);
        List<Media> medias = mediaService.findMediaOfFilms(id_film);
        model.addAttribute("film", film);
        model.addAttribute("medias", medias);

        return "views/media/show-medias";
    }

    @GetMapping("/add")
    public String showAddMediaForm(Model model) {
        Media media = new Media();
        model.addAttribute("media", media);
        return "views/media/add-media";
    }

    @PostMapping("/save")
    public String createMedia(@RequestParam("file") MultipartFile file,
            @ModelAttribute Media media) {
        try {
            String uploadDir = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
                    + File.separator + "resources" + File.separator + "static" + File.separator + "images";

            // Ensure the upload directory exists
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            String fileName = file.getOriginalFilename();
            if (fileName != null && !fileName.isEmpty()) {
                File dest = new File(uploadDir + File.separator + fileName);
                file.transferTo(dest);
                media.setMedia("/images/" + fileName); // Set the media path
            } else {
                throw new IllegalArgumentException("File must not be empty");
            }

            if (media.getId() != null) {
                System.out.println("here");
                Media existingMedia = mediaService.getOne(media.getId());
                if (existingMedia != null) {
                    existingMedia.setFilm(media.getFilm());
                    existingMedia.setMedia(media.getMedia());
                    existingMedia.setMediaType(media.getMediaType());
                    existingMedia.setUpdatedAt(media.getUpdatedAt());

                    mediaService.save(existingMedia);
                }
            } else {
                mediaService.save(media);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/medias/1";
        }
        return "redirect:/medias/1";
    }

}
