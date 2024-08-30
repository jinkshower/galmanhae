package hiyen.galmanhae.dataquery.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import hiyen.galmanhae.data.domain.Congestion;
import hiyen.galmanhae.data.domain.Place;
import hiyen.galmanhae.data.domain.Place.PlaceMapper;
import hiyen.galmanhae.data.domain.Place.Position;
import hiyen.galmanhae.data.domain.Weather;
import hiyen.galmanhae.dataquery.domain.WeatherLevel.WeatherRainingGrade;
import hiyen.galmanhae.dataquery.domain.WeatherLevel.WeatherTempGrade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PlaceDetailsTest {

	@DisplayName("PlaceDetails 객체")
	@Nested
	class create {

		String name = "광화문";
		String code = "GWM123";
		double latitude = 37.571076;
		double longitude = 126.976849;
		int weatherX = 60;
		int weatherY = 127;
		double temp = 25.0;
		double raining = 0.0;
		int people = 10;
		String congestionIndicator = "여유";

		@DisplayName("생성 성공")
		@Test
		void success() {
			final Place place = PlaceMapper.toPlace(
				new Place.PlaceNameAndCode(name, code),
				new Position(latitude, longitude),
				new Place.WeatherPosition(weatherX, weatherY)
			);

			final Weather weather = new Weather(temp, raining);
			final Congestion congestion = new Congestion(people, congestionIndicator);

			final PlaceDetails placeDetails = PlaceDetails.of(place, weather, congestion);

			assertAll(
				() -> assertThat(placeDetails).isNotNull(),
				() -> assertThat(placeDetails.placeName()).isEqualTo(name),
				() -> assertThat(placeDetails.latitude()).isEqualTo(latitude),
				() -> assertThat(placeDetails.longitude()).isEqualTo(longitude),
				() -> assertThat(placeDetails.weatherDescription()).isEqualTo(
					WeatherTempGrade.WARM.getDescription() + " " + WeatherRainingGrade.SUNNY.getDescription()),
				() -> assertThat(placeDetails.weatherTemp()).isEqualTo(temp),
				() -> assertThat(placeDetails.weatherRaining()).isEqualTo(raining),
				() -> assertThat(placeDetails.congestionDescription()).isEqualTo(CongestionLevel.RELAXED.getDescription()),
				() -> assertThat(placeDetails.congestionPeople()).isEqualTo(people),
				() -> assertThat(placeDetails.goOutLevel()).isEqualTo(GoOutLevel.HIGH)
			);
		}

		@DisplayName("생성 성공 - 날씨, 혼잡도 데이터가 없는 경우")
		@Test
		void success_noData() {
			final Place place = PlaceMapper.toPlace(
				new Place.PlaceNameAndCode(name, code),
				new Position(latitude, longitude),
				new Place.WeatherPosition(weatherX, weatherY)
			);

			final Weather weather = new Weather(-1, -1);
			final Congestion congestion = new Congestion(-1, "NONE");

			final PlaceDetails placeDetails = PlaceDetails.of(place, weather, congestion);

			assertAll(
				() -> assertThat(placeDetails).isNotNull(),
				() -> assertThat(placeDetails.placeName()).isEqualTo(name),
				() -> assertThat(placeDetails.latitude()).isEqualTo(latitude),
				() -> assertThat(placeDetails.longitude()).isEqualTo(longitude),
				() -> assertThat(placeDetails.weatherDescription()).isEqualTo(
					WeatherTempGrade.NONE.getDescription() + " " + WeatherRainingGrade.NONE.getDescription()),
				() -> assertThat(placeDetails.weatherTemp()).isEqualTo(-1),
				() -> assertThat(placeDetails.weatherRaining()).isEqualTo(-1),
				() -> assertThat(placeDetails.congestionDescription()).isEqualTo(CongestionLevel.NONE.getDescription()),
				() -> assertThat(placeDetails.congestionPeople()).isEqualTo(-1),
				() -> assertThat(placeDetails.goOutLevel()).isEqualTo(GoOutLevel.NONE)
			);
		}
	}
}
