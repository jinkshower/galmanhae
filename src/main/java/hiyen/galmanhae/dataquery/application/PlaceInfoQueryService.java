package hiyen.galmanhae.dataquery.application;

import hiyen.galmanhae.dataquery.response.PlaceInfoResponse;
import hiyen.galmanhae.dataquery.response.PlaceSearchResponse;
import hiyen.galmanhae.place.domain.place.Place;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo;
import hiyen.galmanhae.place.repository.PlaceInfoRepository;
import hiyen.galmanhae.place.repository.PlaceRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceInfoQueryService {

	private final PlaceInfoRepository placeInfoRepository;
	private final PlaceRepository placeRepository;

	public List<PlaceInfoResponse> getPlaceInfos() {
		List<PlaceInfoResponse> response = new ArrayList<>();
		List<PlaceInfo> all = placeInfoRepository.findAll();
		for (PlaceInfo placeInfo : all) {
			Place place = placeRepository.findByName(placeInfo.getAreaName());
			response.add(PlaceInfoResponse.of(placeInfo, place.getGoOutLevel()));
		}
		return response;
	}

	public List<PlaceSearchResponse> search(final String keyword) {
		final List<PlaceInfo> found = placeInfoRepository.search(keyword);
		return found.stream()
			.map(PlaceSearchResponse::from)
			.toList();
	}
}
