package hiyen.galmanhae.dataquery.application;

import hiyen.galmanhae.place.domain.place.Place;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo;
import hiyen.galmanhae.place.repository.PlaceInfoRepository;
import hiyen.galmanhae.place.repository.PlaceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceQueryService {

	private final PlaceInfoRepository placeInfoRepository;
	private final PlaceRepository placeRepository;

	public List<PlaceInfo> getPlaceInfos() {
		return placeInfoRepository.findAll();
	}

	public Place getPlace(final String placeName) {
		return placeRepository.findByName(placeName);
	}
}
