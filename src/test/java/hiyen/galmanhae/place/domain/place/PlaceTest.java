package hiyen.galmanhae.place.domain.place;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import hiyen.galmanhae.place.domain.place.Place.GoOutLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PlaceTest {
	static class PlaceMapper {

		static Place toPlace(
			final String name,
			final Weather weather,
			final Congestion congestion) {
			return Place.builder()
				.name(name)
				.weather(weather)
				.congestion(congestion)
				.goOutLevel(GoOutLevel.of(weather, congestion))
				.build();
		}
	}

	@DisplayName("Place 객체")
	@Nested
	class create {

		String name = "광화문";
		double weatherTemp = 20.0;
		double weatherRaining = 4.0;
		int congestionPeople = 32400;
		String congestionIndicator = "보통";

		@DisplayName("생성 성공")
		@Test
		void success() {

			final Weather weather = Weather.of(weatherTemp, weatherRaining);
			final Congestion congestion = Congestion.of(congestionPeople, congestionIndicator);

			assertDoesNotThrow(() -> {
				Place place = PlaceMapper.toPlace(name, weather, congestion);
				assertThat(place).isNotNull();
				assertThat(place.goOutLevel()).isNotNull();
			});
		}

		@DisplayName("생성 실패 - 각 필드가 null일 경우")
		@Test
		void fail_nullValues() {
			assertAll(
				//Name이 null
				() -> assertThatThrownBy(() -> {
					PlaceMapper.toPlace(null, Weather.of(weatherTemp, weatherRaining),
						Congestion.of(congestionPeople, congestionIndicator));
				}).isInstanceOf(IllegalArgumentException.class),

				// WeatherTemp가 null
				() -> assertThatThrownBy(() -> {
					Weather weather = Weather.of(null, weatherRaining);
					PlaceMapper.toPlace(name, weather,
						Congestion.of(congestionPeople, congestionIndicator));
				}).isInstanceOf(IllegalArgumentException.class),

				// WeatherRaining이 null
				() -> assertThatThrownBy(() -> {
					Weather weather = Weather.of(weatherTemp, null);
					PlaceMapper.toPlace(name, weather,
						Congestion.of(congestionPeople, congestionIndicator));
				}).isInstanceOf(IllegalArgumentException.class),

				// CongestionPeople이 null
				() -> assertThatThrownBy(() -> {
					Congestion congestion = Congestion.of(null, congestionIndicator);
					PlaceMapper.toPlace(name, Weather.of(weatherTemp, weatherRaining), congestion);
				}).isInstanceOf(IllegalArgumentException.class),

				// CongestionIndicator가 null
				() -> assertThatThrownBy(() -> {
					Congestion congestion = Congestion.of(congestionPeople, null);
					PlaceMapper.toPlace(name, Weather.of(weatherTemp, weatherRaining), congestion);
				}).isInstanceOf(IllegalArgumentException.class)
			);
		}
	}
}
