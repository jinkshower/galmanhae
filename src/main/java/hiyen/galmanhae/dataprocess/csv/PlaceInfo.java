package hiyen.galmanhae.dataprocess.csv;

public record PlaceInfo(
	AreaInfo areaInfo,
	LocationInfo locationInfo,
	WeatherInfo weatherInfo
) {

	public record AreaInfo(
		String areaCode,
		String areaName
	) {

	}

	public record LocationInfo(
		String latitude,
		String longitude
	) {

	}

	public record WeatherInfo(
		String latitude,
		String longitude
	) {

	}
}
