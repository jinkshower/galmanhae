package hiyen.galmanhae.data.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import hiyen.galmanhae.data.domain.Place.PlaceMapper;
import hiyen.galmanhae.data.domain.Place.PlaceNameAndCode;
import hiyen.galmanhae.data.domain.Place.Position;
import hiyen.galmanhae.data.domain.Place.WeatherPosition;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PlaceTest {

	@DisplayName("Place 객체")
	@Nested
	class create {

		String name = "광화문";
		String code = "GWM123";
		double latitude = 37.571076;
		double longitude = 126.976849;
		int weatherX = 60;
		int weatherY = 127;

		@DisplayName("생성 성공")
		@Test
		void success() {
			final PlaceNameAndCode placeNameAndCode = new PlaceNameAndCode(name, code);
			final Position position = new Position(latitude, longitude);
			final WeatherPosition weatherPosition = new WeatherPosition(weatherX, weatherY);

			assertDoesNotThrow(() -> {
				Place place = PlaceMapper.toPlace(placeNameAndCode, position, weatherPosition);
				assertThat(place).isNotNull();
				assertThat(place.placeNameAndCode()).isNotNull();
			});
		}

		@DisplayName("생성 실패 - 각 필드가 null일 경우")
		@Test
		@Disabled
			//TODO 객체 검증 로직 완성 이후 다시 테스트 리팩토링 예정
		void fail_nullValues() {
			assertAll(
				// PlaceNameAndCode가 null
				() -> assertThatThrownBy(() -> {
					PlaceMapper.toPlace(null, new Position(latitude, longitude),
						new WeatherPosition(weatherX, weatherY));
				}).isInstanceOf(IllegalArgumentException.class),

				// Position이 null
				() -> assertThatThrownBy(() -> PlaceMapper.toPlace(new PlaceNameAndCode(name, code), null,
					new WeatherPosition(weatherX, weatherY))).isInstanceOf(IllegalArgumentException.class),

				// WeatherPosition이 null
				() -> assertThatThrownBy(() -> PlaceMapper.toPlace(new PlaceNameAndCode(name, code), new Position(latitude, longitude),
					null)).isInstanceOf(IllegalArgumentException.class)
			);
		}
	}
}
