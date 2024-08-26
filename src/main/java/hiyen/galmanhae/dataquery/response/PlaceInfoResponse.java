package hiyen.galmanhae.dataquery.response;

import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo;

public record PlaceInfoResponse(
	String name,
	String latitude,
	String longitude,
	String goOutLevel
) {

	public static PlaceInfoResponse of(PlaceInfo placeInfo, String goOutLevelDescription) {
		return new PlaceInfoResponse(
			placeInfo.areaInfo().areaName(),
			placeInfo.locationInfo().latitude(),
			placeInfo.locationInfo().longitude(),
			goOutLevelDescription
		);
	}
}
