package hiyen.galmanhae.dataquery.response;

import hiyen.galmanhae.dataquery.domain.PlaceDetails;

public record PlaceDetailResponse(

	String name,

	String goOutLevelDescription,

	String weatherDescription,

	double weatherTemp,

	double weatherRaining,

	String congestionDescription,

	int congestionPeople
) {

	public static PlaceDetailResponse from(final PlaceDetails placeDetails) {
		return new PlaceDetailResponse(
			placeDetails.placeName(),
			placeDetails.goOutLevel().getDescription(),
			placeDetails.weatherDescription(),
			placeDetails.weatherTemp(),
			placeDetails.weatherRaining(),
			placeDetails.congestionDescription(),
			placeDetails.congestionPeople());
	}
}
