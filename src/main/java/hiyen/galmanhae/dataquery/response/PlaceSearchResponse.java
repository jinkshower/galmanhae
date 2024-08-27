package hiyen.galmanhae.dataquery.response;

import hiyen.galmanhae.data.domain.Place;

public record PlaceSearchResponse(
	Long placeId,
	String placeName,
	double latitude,
	double longitude
) {

	public static PlaceSearchResponse from(final Place place) {
		return new PlaceSearchResponse(
			place.id(),
			place.placeNameAndCode().name(),
			place.position().latitude(),
			place.position().longitude());
	}
}
