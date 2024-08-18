package hiyen.galmanhae.dataprocess.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WeatherBaseTimeTest {

	@DisplayName("시간을 입력하면 알맞은 BaseTime을 반환한다.")
	@Test
	void success() {
		assertAll(
			() -> assertThat(WeatherBaseTime.of(LocalDateTime.of(2024, 8, 18, 1, 0)))
				.isEqualTo(WeatherBaseTime.BASE_TIME_2300),

			() -> assertThat(WeatherBaseTime.of(LocalDateTime.of(2024, 8, 18, 2, 0)))
				.isEqualTo(WeatherBaseTime.BASE_TIME_2300),

			() -> assertThat(WeatherBaseTime.of(LocalDateTime.of(2024, 8, 18, 4, 59)))
				.isEqualTo(WeatherBaseTime.BASE_TIME_0200),

			() -> assertThat(WeatherBaseTime.of(LocalDateTime.of(2024, 8, 18, 5, 10)))
				.isEqualTo(WeatherBaseTime.BASE_TIME_0500),

			() -> assertThat(WeatherBaseTime.of(LocalDateTime.of(2024, 8, 18, 7, 59)))
				.isEqualTo(WeatherBaseTime.BASE_TIME_0500),

			() -> assertThat(WeatherBaseTime.of(LocalDateTime.of(2024, 8, 18, 8, 10)))
				.isEqualTo(WeatherBaseTime.BASE_TIME_0800),

			() -> assertThat(WeatherBaseTime.of(LocalDateTime.of(2024, 8, 18, 10, 59)))
				.isEqualTo(WeatherBaseTime.BASE_TIME_0800),

			() -> assertThat(WeatherBaseTime.of(LocalDateTime.of(2024, 8, 18, 11, 10)))
				.isEqualTo(WeatherBaseTime.BASE_TIME_1100),

			() -> assertThat(WeatherBaseTime.of(LocalDateTime.of(2024, 8, 18, 13, 59)))
				.isEqualTo(WeatherBaseTime.BASE_TIME_1100),

			() -> assertThat(WeatherBaseTime.of(LocalDateTime.of(2024, 8, 18, 14, 10)))
				.isEqualTo(WeatherBaseTime.BASE_TIME_1400),

			() -> assertThat(WeatherBaseTime.of(LocalDateTime.of(2024, 8, 18, 16, 59)))
				.isEqualTo(WeatherBaseTime.BASE_TIME_1400),

			() -> assertThat(WeatherBaseTime.of(LocalDateTime.of(2024, 8, 18, 17, 10)))
				.isEqualTo(WeatherBaseTime.BASE_TIME_1700),

			() -> assertThat(WeatherBaseTime.of(LocalDateTime.of(2024, 8, 18, 19, 59)))
				.isEqualTo(WeatherBaseTime.BASE_TIME_1700),

			() -> assertThat(WeatherBaseTime.of(LocalDateTime.of(2024, 8, 18, 20, 10)))
				.isEqualTo(WeatherBaseTime.BASE_TIME_2000),

			() -> assertThat(WeatherBaseTime.of(LocalDateTime.of(2024, 8, 18, 22, 59)))
				.isEqualTo(WeatherBaseTime.BASE_TIME_2000),

			() -> assertThat(WeatherBaseTime.of(LocalDateTime.of(2024, 8, 18, 23, 23)))
				.isEqualTo(WeatherBaseTime.BASE_TIME_2300)
		);
	}
}
