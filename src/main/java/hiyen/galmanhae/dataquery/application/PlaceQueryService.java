package hiyen.galmanhae.dataquery.application;

import hiyen.galmanhae.place.domain.place.Place;
import hiyen.galmanhae.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceQueryService {

	private final PlaceRepository placeRepository;

	public Place getPlace(final String placeName) {
		return placeRepository.findByName(placeName);
	}
}
