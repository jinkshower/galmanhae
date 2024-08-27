package hiyen.galmanhae.data.domain;

import lombok.Builder;

public record Place(

	Long id,
	PlaceNameAndCode placeNameAndCode,
	Position position,
	WeatherPosition weatherPosition
) {

	public Place(
		final Long id,
		final PlaceNameAndCode placeNameAndCode,
		final Position position,
		final WeatherPosition weatherPosition) {
		this.id = id;
		this.placeNameAndCode = placeNameAndCode;
		this.position = position;
		this.weatherPosition = weatherPosition;
	}

	@Builder
	public Place(
		final PlaceNameAndCode placeNameAndCode,
		final Position position,
		final WeatherPosition weatherPosition) {
		this(null, placeNameAndCode, position, weatherPosition);
	}

	public record PlaceNameAndCode(
		String name,
		String code
	) {

	}

	public record Position(
		double latitude,
		double longitude
	) {

	}

	public record WeatherPosition(
		int weatherX,
		int weatherY
	) {

	}

	public static class PlaceMapper {

		public static Place toPlace(
			final PlaceNameAndCode placeNameAndCode,
			final Position position,
			final WeatherPosition weatherPosition) {
			return Place.builder()
				.placeNameAndCode(placeNameAndCode)
				.position(position)
				.weatherPosition(weatherPosition)
				.build();
		}
	}
}
