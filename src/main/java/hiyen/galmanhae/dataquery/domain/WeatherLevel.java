package hiyen.galmanhae.dataquery.domain;

import hiyen.galmanhae.data.domain.Weather;
import java.util.Arrays;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record WeatherLevel(
	WeatherRainingGrade weatherRainingGrade,
	WeatherTempGrade weatherTempGrade,
	Integer weatherScore
) {

	public static WeatherLevel of(final Weather weather) {
		final WeatherTempGrade tempGrade = WeatherTempGrade.of(weather.temperature());
		final WeatherRainingGrade rainingGrade = WeatherRainingGrade.of(weather.rainingProbability());
		final int score = rainingGrade.getScore() + tempGrade.getScore();

		return new WeatherLevel(rainingGrade, tempGrade, score);
	}

	public String getDescription() {
		return weatherTempGrade.getDescription() + " " + weatherRainingGrade.getDescription();
	}

	@Getter
	@RequiredArgsConstructor
	public enum WeatherRainingGrade {
		SUNNY("맑아요", 40, probability -> probability >= 0 && probability <= 10),
		CLOUDY("흐려요", 20, probability -> probability > 10 && probability <= 50),
		RAINY("비와요", 5, probability -> probability > 50),
		NONE(" ", 0, probability -> true);

		private final String description;
		private final int score;

		private final Predicate<Double> condition;

		public static WeatherRainingGrade of(final Double rainingProbability) {
			return Arrays.stream(values())
				.filter(grade -> grade.condition.test(rainingProbability))
				.findFirst()
				.orElse(NONE);
		}
	}

	@Getter
	@RequiredArgsConstructor
	public enum WeatherTempGrade {
		COLD("춥고", 5, temp -> temp >= 0 && temp <= 5),
		CHILLY("쌀쌀하고", 20, temp -> temp > 5 && temp <= 10),
		MILD("시원하고", 40, temp -> temp > 10 && temp <= 20),
		WARM("따뜻하고", 20, temp -> temp > 20 && temp <= 30),
		HOT("덥고", 5, temp -> temp > 30),
		NONE(" ", 0, temp -> true);

		private final String description;
		private final int score;

		private final Predicate<Double> condition;

		public static WeatherTempGrade of(final Double weatherTemp) {
			return Arrays.stream(values())
				.filter(grade -> grade.condition.test(weatherTemp))
				.findFirst()
				.orElse(NONE);
		}
	}
}
