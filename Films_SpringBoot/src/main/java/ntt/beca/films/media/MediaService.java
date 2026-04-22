package ntt.beca.films.media;

import java.util.List;

import org.springframework.stereotype.Service;

import ntt.beca.films.shared.service.CrudService;
import ntt.beca.films.shared.service.PagedResultDto;

@Service
public class MediaService implements CrudService<Media, Long> {

	private MediaRepository mediaRepository;

	// constructor and repository injection
	public MediaService(MediaRepository mediaRepository) {
		this.mediaRepository = mediaRepository;
	}

	// saving method
	@Override
	public void save(Media t) {
		mediaRepository.save(t);
	}

	// getting one Media by Id
	@Override
	public Media getOne(Long id) {
		return mediaRepository.findById(id).get();
	}

	// deleting one Media by Id
	@Override
	public boolean delete(Long id) {
		if (mediaRepository.existsById(id)) {
			mediaRepository.deleteById(id);
			return true;
		}
		return false;
	}

	// getting all the medias of films using pagination
	@Override
	public PagedResultDto<Media> getAll(int pageNumber, String keyword, String genre) {

		return null;

	}

	public List<Media> findMediaOfFilms(Long id) {
		List<Media> medias = mediaRepository.findMediasOfFilms(id);
		return medias;
	}

}
