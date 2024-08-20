package hiyen.galmanhae.dataprocess.application;

import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo;
import hiyen.galmanhae.place.domain.place.Place;
import hiyen.galmanhae.place.entity.PlaceInfoEntity;
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
		//TODO 벌크 insert로 변경예정
		placeRepository.saveAll(places);
	}

	public void saveAllPlaceInfos(final List<PlaceInfo> placeInfos) {
		//TODO 벌크 insert로 변경예정
		final List<PlaceInfoEntity> list = placeInfos.stream()
			.map(PlaceInfoEntity::from)
			.toList();
		placeInfoRepository.saveAll(list);
	}

	public List<PlaceInfo> findAllPlaceInfos() {
		final List<PlaceInfoEntity> found = placeInfoRepository.findAll();
		return found.stream()
			.map(PlaceInfoEntity::toPlaceInfo)
			.toList();
	}
}
