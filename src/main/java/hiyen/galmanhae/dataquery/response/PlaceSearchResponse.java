package hiyen.galmanhae.dataquery.response;

import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo;

public record PlaceSearchResponse(

	String placeName
) {

	public static PlaceSearchResponse from(final PlaceInfo placeInfo) {
		return new PlaceSearchResponse(placeInfo.getAreaName());
	}
}
