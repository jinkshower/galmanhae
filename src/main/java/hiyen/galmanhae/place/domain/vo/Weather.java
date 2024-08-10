package hiyen.galmanhae.place.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Weather {

	@Column(name = "weather_temp")
	private Double weatherTemp;

	@Column(name = "weather_raining")
	private Double weatherRaining;

	@Embedded
	private WeatherLevel weatherLevel;

	public static Weather of(final Double weatherTemp, final Double weatherRaining) {
		if (Objects.isNull(weatherTemp)) {
			log.warn("기온이 없어요. 시간: {}", LocalDateTime.now());
			throw new IllegalArgumentException();
		}
		if (Objects.isNull(weatherRaining)) {
			log.warn("강수확률이 없어요. 시간: {}", LocalDateTime.now());
			throw new IllegalArgumentException();
		}

		return new Weather(weatherTemp, weatherRaining, WeatherLevel.of(weatherTemp, weatherRaining));
	}

	public int getScore() {
		return weatherLevel.getWeatherScore();
	}
}
