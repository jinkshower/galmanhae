package hiyen.galmanhae.dataprocess;

import hiyen.galmanhae.dataprocess.application.CongestionService;
import hiyen.galmanhae.dataprocess.application.DataQueryService;
import hiyen.galmanhae.dataprocess.application.WeatherService;
import hiyen.galmanhae.place.domain.place.Congestion;
import hiyen.galmanhae.place.domain.place.Location;
import hiyen.galmanhae.place.domain.place.Place;
import hiyen.galmanhae.place.domain.place.Place.GoOutLevel;
import hiyen.galmanhae.place.domain.place.Weather;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo.AreaInfo;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo.LocationInfo;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo.WeatherInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataProcessor {

	private final WeatherService weatherService;
	private final CongestionService congestionService;
	private final DataQueryService dataQueryService;

	/**
	 * CSV 파일의 장소 정보를 토대로 외부 API를 호출하여 데이터를 가져오고 DB에 저장
	 * 외부 API 호출 및 데이터 가져오기에 실패한 경우, 로그를 남기고 해당 데이터는 저장하지 않음
	 */
	public void process() {
		final List<PlaceInfo> placeInfos = dataQueryService.findAllPlaceInfos();
		final List<CompletableFuture<Place>> futures = new ArrayList<>();

		for (final PlaceInfo placeInfo : placeInfos) {
			futures.add(toFuture(placeInfo));
		}

		final List<Place> places = futures.stream()
			.map(CompletableFuture::join)
			.filter(Objects::nonNull)
			.toList();

		dataQueryService.saveAllPlaces(places);
	}

	private CompletableFuture<Place> toFuture(final PlaceInfo placeInfo) {
		return CompletableFuture.supplyAsync(() -> aggregatePlace(placeInfo))
			.exceptionally(exception -> {
				log.warn("외부 API 호출 및 가져오기에 실패했습니다: {}. Error: {}", placeInfo, exception.getMessage(), exception);
				return null;
			});
	}

	private Place aggregatePlace(final PlaceInfo placeInfo) {
		final AreaInfo areaInfo = placeInfo.areaInfo();
		final LocationInfo locationInfo = placeInfo.locationInfo();
		final WeatherInfo weatherInfo = placeInfo.weatherInfo();

		final Congestion congestion = congestionService.fetch(areaInfo.areaCode());
		final Weather weather = weatherService.fetch(weatherInfo.weatherX(), weatherInfo.weatherY());
		final Location location = Location.of(Double.valueOf(locationInfo.latitude()), Double.valueOf(locationInfo.longitude()));

		return PlaceMapper.toPlace(areaInfo.areaName(), location, weather, congestion);
	}

	private static class PlaceMapper {

		private static Place toPlace(
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
