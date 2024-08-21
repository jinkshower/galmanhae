package hiyen.galmanhae.place.entity;

import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo.AreaInfo;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo.LocationInfo;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo.WeatherInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "place_info")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlaceInfoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private AreaInfoEntity areaInfo;

	@Embedded
	private LocationInfoEntity locationInfo;

	@Embedded
	private WeatherInfoEntity weatherInfo;

	@Builder
	public PlaceInfoEntity(final AreaInfoEntity areaInfo, final LocationInfoEntity locationInfo, final WeatherInfoEntity weatherInfo) {
		this(null, areaInfo, locationInfo, weatherInfo);
	}

	public static PlaceInfoEntity from(final PlaceInfo placeInfo) {
		return PlaceInfoEntity.builder()
			.areaInfo(new AreaInfoEntity(placeInfo.areaInfo().areaCode(), placeInfo.areaInfo().areaName()))
			.locationInfo(new LocationInfoEntity(placeInfo.locationInfo().latitude(), placeInfo.locationInfo().longitude()))
			.weatherInfo(new WeatherInfoEntity(placeInfo.weatherInfo().weatherX(), placeInfo.weatherInfo().weatherY()))
			.build();
	}

	public static PlaceInfo toPlaceInfo(final PlaceInfoEntity placeInfoEntity) {
		return new PlaceInfo(
			new AreaInfo(placeInfoEntity.getAreaInfo().getAreaCode(), placeInfoEntity.getAreaInfo().getAreaName()),
			new LocationInfo(placeInfoEntity.getLocationInfo().getLatitude(), placeInfoEntity.getLocationInfo().getLongitude()),
			new WeatherInfo(placeInfoEntity.getWeatherInfo().getWeatherX(), placeInfoEntity.getWeatherInfo().getWeatherY())
		);
	}

	@Embeddable
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class AreaInfoEntity {

		@Column(name = "area_code", length = 10)
		private String areaCode;

		@Column(name = "area_name")
		private String areaName;
	}

	@Embeddable
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class LocationInfoEntity {

		@Column(name = "latitude")
		private String latitude;

		@Column(name = "longitude")
		private String longitude;
	}

	@Embeddable
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class WeatherInfoEntity {

		@Column(name = "weather_x", length = 5)
		private String weatherX;

		@Column(name = "weather_y", length = 5)
		private String weatherY;
	}
}

