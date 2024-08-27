package hiyen.galmanhae.dataquery.response;

import hiyen.galmanhae.dataquery.domain.PlaceDetails;

public record PlaceResponse(

	Long placeId,
	String name,
	double latitude,
	double longitude,
	String goOutLevel
) {

	public static PlaceResponse from(final Long id, final PlaceDetails placeDetails) {
		return new PlaceResponse(
			id,
			placeDetails.placeName(),
			placeDetails.latitude(),
			placeDetails.longitude(),
			placeDetails.goOutLevel().name()
		);
	}
}
