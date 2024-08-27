package hiyen.galmanhae.dataquery.domain;

import hiyen.galmanhae.data.domain.Congestion;
import hiyen.galmanhae.data.domain.Place;
import hiyen.galmanhae.data.domain.Weather;

public record PlaceDetails(
	String placeName,
	double latitude,
	double longitude,
	String weatherDescription,
	double weatherTemp,
	double weatherRaining,
	String congestionDescription,
	int congestionPeople,
	GoOutLevel goOutLevel
) {

	public static PlaceDetails of(final Place place, final Weather weather, final Congestion congestion) {
		final WeatherLevel weatherLevel = WeatherLevel.of(weather);
		final CongestionLevel congestionLevel = CongestionLevel.of(congestion);
		final GoOutLevel goOutLevel = GoOutLevel.of(weatherLevel, congestionLevel);

		return new PlaceDetails(
			place.placeNameAndCode().name(),
			place.position().latitude(),
			place.position().longitude(),
			weatherLevel.getDescription(),
			weather.temperature(),
			weather.rainingProbability(),
			congestionLevel.getDescription(),
			congestion.currentPeople(),
			goOutLevel
		);
	}
}
