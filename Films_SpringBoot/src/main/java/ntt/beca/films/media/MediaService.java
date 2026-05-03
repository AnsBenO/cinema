package ntt.beca.films.media;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ntt.beca.films.shared.exception.ResourceNotFoundException;
import ntt.beca.films.shared.service.CrudService;
import ntt.beca.films.shared.service.PagedResultDto;

@RequiredArgsConstructor
@Service
public class MediaService implements CrudService<MediaDto, Long> {

	private final MediaMapper mediaMapper;

	private final MediaRepository mediaRepository;

	// saving method
	@Override
	public void save(@NonNull MediaDto dto) {
		Media media = mediaMapper.toEntity(dto);
		if (media != null) {
			mediaRepository.save(media);
		}
	}

	// getting one Media by Id
	@Override
	public MediaDto getOne(@NonNull Long id) {
		Media media = mediaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Media not found"));
		return mediaMapper.toDto(media);
	}

	// deleting one Media by Id
	@Override
	public boolean delete(@NonNull Long id) {
		if (mediaRepository.existsById(id)) {
			mediaRepository.deleteById(id);
			return true;
		}
		return false;
	}

	// getting all the medias of films using pagination
	@Override
	public PagedResultDto<MediaDto> getAll(int pageNumber, String keyword, String genre) {
		// TODO
		return null;
	}

	public List<MediaDto> findMediaOfFilms(Long id) {
		List<Media> medias = mediaRepository.findMediasOfFilms(id);
		return medias.stream()
				.map(mediaMapper::toDto)
				.toList();
	}

}
