package hiyen.galmanhae.dataquery.response;

import hiyen.galmanhae.place.domain.place.Place;

public record PlaceResponse(

	String name,

	String goOutLevelDescription,

	String weatherDescription,

	double weatherTemp,

	double weatherRaining,

	String congestionDescription,

	int congestionPeople
) {

	public static PlaceResponse from(final Place place) {
		return new PlaceResponse(
			place.name(),
			place.getGoOutLevelDescription(),
			place.getWeatherDescription(),
			place.getWeatherTemp(),
			place.getWeatherRaining(),
			place.getCongestionDescription(),
			place.getCongestionPeople()
		);
	}
}
