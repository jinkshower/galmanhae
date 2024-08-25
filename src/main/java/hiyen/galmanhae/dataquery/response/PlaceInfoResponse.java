package hiyen.galmanhae.dataquery.response;

import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo;
import java.util.List;

public record PlaceInfoResponse(
	String name,
	String latitude,
	String longitude
) {

	public static List<PlaceInfoResponse> listOf(final List<PlaceInfo> placeInfos) {
		return placeInfos.stream()
			.map(PlaceInfoResponse::of)
			.toList();
	}

	private static PlaceInfoResponse of(PlaceInfo placeInfo) {
		return new PlaceInfoResponse(
			placeInfo.areaInfo().areaName(),
			placeInfo.locationInfo().latitude(),
			placeInfo.locationInfo().longitude()
		);
	}
}
