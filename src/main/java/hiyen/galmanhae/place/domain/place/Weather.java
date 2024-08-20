package hiyen.galmanhae.place.domain.place;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record Weather(
	Double weatherTemp,
	Double weatherRaining,
	WeatherLevel weatherLevel
) {

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
		return weatherLevel.weatherScore();
	}
}
