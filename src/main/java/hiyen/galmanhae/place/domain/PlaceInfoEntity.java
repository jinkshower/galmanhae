package hiyen.galmanhae.place.domain;

import hiyen.galmanhae.dataprocess.csv.PlaceInfo;
import hiyen.galmanhae.dataprocess.csv.PlaceInfo.AreaInfo;
import hiyen.galmanhae.dataprocess.csv.PlaceInfo.LocationInfo;
import hiyen.galmanhae.dataprocess.csv.PlaceInfo.WeatherInfo;
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

@Entity
@Table(name = "place_info")
@Getter
@AllArgsConstructor
@NoArgsConstructor
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

		private String areaCode;
		private String areaName;
	}

	@Embeddable
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class LocationInfoEntity {

		private String latitude;
		private String longitude;
	}

	@Embeddable
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class WeatherInfoEntity {

		private String weatherX;
		private String weatherY;
	}
}

