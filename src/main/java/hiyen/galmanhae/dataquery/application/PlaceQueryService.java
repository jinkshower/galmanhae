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
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceQueryService {

	private static final List<Place> cache = new ArrayList<>();

	private final PlaceRepository placeRepository;
	private final WeatherRepository weatherRepository;
	private final CongestionRepository congestionRepository;

	@PostConstruct
	public void init() {
		final List<Place> allPlaces = placeRepository.findAll();
		cache.addAll(allPlaces);
	}

	public List<PlaceResponse> getAllPlaces() {
		final List<Place> allPlaces = placeRepository.findAll();
		final List<Long> placeIds = allPlaces.stream()
			.map(Place::id)
			.toList();

		final List<Weather> weathers = weatherRepository.findMostRecentByPlaceIds(placeIds);
		final List<Congestion> congestions = congestionRepository.findMostRecentByPlaceIds(placeIds);

		final Map<Long, Weather> weatherMap = weathers.stream()
			.collect(Collectors.toMap(Weather::placeId, weather -> weather));
		final Map<Long, Congestion> congestionMap = congestions.stream()
			.collect(Collectors.toMap(Congestion::placeId, congestion -> congestion));

		return allPlaces.stream()
			.map(place -> {
				final Weather weather = weatherMap.getOrDefault(place.id(), Weather.of(-1, -1));
				final Congestion congestion = congestionMap.getOrDefault(place.id(), Congestion.of(-1, "NONE"));
				final PlaceDetails placeDetails = PlaceDetails.of(place, weather, congestion);
				return PlaceResponse.from(place.id(), placeDetails);
			})
			.toList();
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

	public List<PlaceSearchResponse> autoComplete(final String keyword) {
		return cache.stream()
			.filter(place -> place.placeNameAndCode().name().startsWith(keyword))
			.map(PlaceSearchResponse::from)
			.toList();
	}
}
