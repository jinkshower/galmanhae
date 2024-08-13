package hiyen.galmanhae.dataprocess;

import hiyen.galmanhae.dataprocess.application.CongestionService;
import hiyen.galmanhae.dataprocess.application.DataSaveService;
import hiyen.galmanhae.dataprocess.application.WeatherService;
import hiyen.galmanhae.dataprocess.csv.CSVDataStore;
import hiyen.galmanhae.dataprocess.csv.PlaceInfo;
import hiyen.galmanhae.dataprocess.csv.PlaceInfo.AreaInfo;
import hiyen.galmanhae.dataprocess.csv.PlaceInfo.LocationInfo;
import hiyen.galmanhae.dataprocess.csv.PlaceInfo.WeatherInfo;
import hiyen.galmanhae.place.domain.Place;
import hiyen.galmanhae.place.domain.Place.GoOutLevel;
import hiyen.galmanhae.place.domain.vo.Congestion;
import hiyen.galmanhae.place.domain.vo.Location;
import hiyen.galmanhae.place.domain.vo.Weather;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataProcessor {

	private final WeatherService weatherService;
	private final CongestionService congestionService;
	private final DataSaveService dataSaveService;
	private final CSVDataStore csvDataStore;

	public void process() {
		final List<PlaceInfo> placeInfos = csvDataStore.getPlaceInfos();
		final List<Place> places = new ArrayList<>();

		for (final PlaceInfo placeInfo : placeInfos) {
			final Place place = aggregatePlace(placeInfo);
			places.add(place);
			break;
		}

		dataSaveService.saveAll(places);
	}

	private Place aggregatePlace(final PlaceInfo placeInfo) {
		final AreaInfo areaInfo = placeInfo.areaInfo();
		final LocationInfo locationInfo = placeInfo.locationInfo();
		final WeatherInfo weatherInfo = placeInfo.weatherInfo();

		final Congestion congestion = congestionService.fetch(areaInfo.areaCode());
		final Weather weather = weatherService.fetch(weatherInfo.latitude(), weatherInfo.longitude());
		final Location location = Location.of(Double.valueOf(locationInfo.latitude()), Double.valueOf(locationInfo.longitude()));

		return PlaceMapper.toPlace(areaInfo.areaName(), location, weather, congestion);
	}

	static class PlaceMapper {

		public static Place toPlace(
			final String name,
			final Location location,
			final Weather weather,
			final Congestion congestion) {
			return Place.builder()
				.name(name)
				.location(location)
				.weather(weather)
				.congestion(congestion)
				.goOutLevel(GoOutLevel.of(weather, congestion))
				.build();
		}
	}
}
