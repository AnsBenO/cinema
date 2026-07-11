package com.ansbeno.films.media;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ansbeno.films.shared.exception.ResourceNotFoundException;
import com.ansbeno.films.shared.service.CrudService;
import com.ansbeno.films.shared.service.PagedResultDto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
