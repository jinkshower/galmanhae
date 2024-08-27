package hiyen.galmanhae.dataprocess.application;

import hiyen.galmanhae.data.domain.Place;
import hiyen.galmanhae.data.domain.WeatherAndCongestion;
import hiyen.galmanhae.data.repository.CongestionRepository;
import hiyen.galmanhae.data.repository.PlaceRepository;
import hiyen.galmanhae.data.repository.WeatherRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataQueryService {

	private final PlaceRepository placeRepository;
	private final WeatherRepository weatherRepository;
	private final CongestionRepository congestionRepository;

	public void saveAllWeatherAndCongestions(final List<WeatherAndCongestion> weatherAndCongestions) {
		weatherRepository.saveAll(weatherAndCongestions);
		congestionRepository.saveAll(weatherAndCongestions);
	}

	public List<Place> findAllPlaces() {
		return placeRepository.findAll();
	}

	public void saveAllPlaces(final List<Place> places) {
		placeRepository.saveAll(places);
	}

	public void deleteAllPlaces() {
		placeRepository.deleteAll();
	}
}
