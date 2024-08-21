package hiyen.galmanhae.dataprocess.application;

import hiyen.galmanhae.place.domain.place.Place;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo;
import hiyen.galmanhae.place.repository.PlaceInfoRepository;
import hiyen.galmanhae.place.repository.PlaceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataQueryService {

	private final PlaceRepository placeRepository;
	private final PlaceInfoRepository placeInfoRepository;

	public void saveAllPlaces(final List<Place> places) {
		placeRepository.saveAll(places);
	}

	public void saveAllPlaceInfos(final List<PlaceInfo> placeInfos) {
		placeInfoRepository.saveAll(placeInfos);
	}

	public List<PlaceInfo> findAllPlaceInfos() {
		return placeInfoRepository.findAll();
	}
}
