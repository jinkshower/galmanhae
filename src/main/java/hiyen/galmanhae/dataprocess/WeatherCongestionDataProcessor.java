package hiyen.galmanhae.dataprocess;

import hiyen.galmanhae.data.domain.Congestion;
import hiyen.galmanhae.data.domain.Place;
import hiyen.galmanhae.data.domain.Place.PlaceNameAndCode;
import hiyen.galmanhae.data.domain.Place.WeatherPosition;
import hiyen.galmanhae.data.domain.Weather;
import hiyen.galmanhae.data.domain.WeatherAndCongestion;
import hiyen.galmanhae.dataprocess.application.CongestionFetchService;
import hiyen.galmanhae.dataprocess.application.DataQueryService;
import hiyen.galmanhae.dataprocess.application.WeatherFetchService;
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
public class WeatherCongestionDataProcessor {

	private final WeatherFetchService weatherFetchService;
	private final CongestionFetchService congestionFetchService;
	private final DataQueryService dataQueryService;

	/**
	 * 장소 정보를 토대로 외부 API를 호출하여 데이터를 가져오고 DB에 저장 외부 API 호출 및 데이터 가져오기에 실패한 경우, 로그를 남기고 해당 데이터는 저장하지 않음
	 */
	public void process() {
		final List<Place> places = dataQueryService.findAllPlaces();
		final List<CompletableFuture<WeatherAndCongestion>> futures = new ArrayList<>();

		for (final Place place : places) {
			futures.add(toFuture(place));
		}

		final List<WeatherAndCongestion> weatherAndCongestions = futures.stream()
			.map(CompletableFuture::join)
			.filter(Objects::nonNull)
			.toList();

		dataQueryService.saveAllWeatherAndCongestions(weatherAndCongestions);
	}

	private CompletableFuture<WeatherAndCongestion> toFuture(final Place place) {
		return CompletableFuture.supplyAsync(() -> fetch(place))
			.exceptionally(exception -> {
				log.warn("외부 API 호출 및 가져오기에 실패했습니다: {}. Error: {}", place, exception.getMessage(), exception);
				return null;
			});
	}

	private WeatherAndCongestion fetch(final Place place) {
		final PlaceNameAndCode placeNameAndCode = place.placeNameAndCode();
		final WeatherPosition weatherPosition = place.weatherPosition();

		final Congestion congestion = congestionFetchService.fetch(placeNameAndCode.code());
		final Weather weather = weatherFetchService.fetch(weatherPosition.weatherX(), weatherPosition.weatherY());

		return new WeatherAndCongestion(place.id(), weather, congestion);
	}
}
