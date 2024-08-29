package hiyen.galmanhae.dataquery.application;

import hiyen.galmanhae.data.domain.Congestion;
import hiyen.galmanhae.data.domain.Place;
import hiyen.galmanhae.data.domain.Weather;
import hiyen.galmanhae.data.repository.CongestionRepository;
import hiyen.galmanhae.data.repository.PlaceRepository;
import hiyen.galmanhae.data.repository.WeatherRepository;
import hiyen.galmanhae.dataquery.domain.PlaceDetails;
import hiyen.galmanhae.dataquery.response.PlaceDetailResponse;
import hiyen.galmanhae.dataquery.response.PlaceResponse;
import hiyen.galmanhae.dataquery.response.PlaceSearchResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceQueryService {

	private final PlaceRepository placeRepository;
	private final WeatherRepository weatherRepository;
	private final CongestionRepository congestionRepository;

	public List<PlaceResponse> getAllPlaces() {
		final List<Place> all = placeRepository.findAll();
		final List<PlaceResponse> responses = new ArrayList<>();

		for (Place place : all) {
			final Weather weather = weatherRepository.findMostRecentByPlaceId(place.id());
			final Congestion congestion = congestionRepository.findMostRecentByPlaceId(place.id());
			final PlaceDetails placeDetails = PlaceDetails.of(place, weather, congestion);

			responses.add(PlaceResponse.from(place.id(), placeDetails));
		}
		return responses;
	}

	public PlaceDetailResponse getPlace(final Long placeId) {
		final Place place = placeRepository.findById(placeId);
		final Weather weather = weatherRepository.findMostRecentByPlaceId(place.id());
		final Congestion congestion = congestionRepository.findMostRecentByPlaceId(place.id());

		final PlaceDetails placeDetails = PlaceDetails.of(place, weather, congestion);
		return PlaceDetailResponse.from(placeDetails);
	}

	public List<PlaceSearchResponse> search(final String keyword) {
		final List<Place> found = placeRepository.search(keyword);
		return found.stream()
			.map(PlaceSearchResponse::from)
			.toList();
	}
}
