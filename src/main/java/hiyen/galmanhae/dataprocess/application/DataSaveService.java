package hiyen.galmanhae.dataprocess.application;

import hiyen.galmanhae.place.domain.Place;
import hiyen.galmanhae.place.domain.repository.PlaceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataSaveService {

	private final PlaceRepository placeRepository;

	public void saveAll(final List<Place> places) {
		placeRepository.saveAll(places);
	}
}
