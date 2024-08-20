package hiyen.galmanhae.place.domain.place;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class WeatherLevel {

	@Column(name = "weather_raining_grade")
	@Enumerated(EnumType.STRING)
	private WeatherRainingGrade weatherRainingGrade;

	@Column(name = "weather_temp_grade")
	@Enumerated(EnumType.STRING)
	private WeatherTempGrade weatherTempGrade;

	@Column(name = "weather_score")
	private Integer weatherScore;

	public static WeatherLevel of(final Double weatherTemp, final Double weatherRaining) {
		final WeatherRainingGrade rainingGrade = WeatherRainingGrade.of(weatherRaining);
		final WeatherTempGrade tempGrade = WeatherTempGrade.of(weatherTemp);
		final int score = rainingGrade.getScore() + tempGrade.getScore();

		return new WeatherLevel(rainingGrade, tempGrade, score);
	}

	@Getter
	@RequiredArgsConstructor
	public enum WeatherRainingGrade {
		SUNNY("맑아요", 40, probability -> probability <= 10),
		CLOUDY("흐려요", 20, probability -> probability <= 50),
		RAINY("비와요", 5, probability -> probability > 50);

		private final String description;
		private final int score;

		private final Predicate<Double> condition;

		public static WeatherRainingGrade of(final Double rainingProbability) {
			return Arrays.stream(values())
				.filter(grade -> grade.condition.test(rainingProbability))
				.findFirst()
				.orElseThrow(() -> {
					log.warn("적절한 강수 등급을 찾지 못했습니다. 강수확률: {}, 시간: {}", rainingProbability, LocalDateTime.now());
					return new IllegalArgumentException();
				});
		}
	}

	@Getter
	@RequiredArgsConstructor
	public enum WeatherTempGrade {
		COLD("춥고", 5, temp -> temp <= 5),
		CHILLY("쌀쌀하고", 20, temp -> temp <= 10),
		MILD("시원하고", 40, temp -> temp <= 20),
		WARM("따뜻하고", 20, temp -> temp <= 30),
		HOT("덥고", 5, temp -> temp > 30);

		private final String description;
		private final int score;

		private final Predicate<Double> condition;

		public static WeatherTempGrade of(final Double weatherTemp) {
			return Arrays.stream(values())
				.filter(grade -> grade.condition.test(weatherTemp))
				.findFirst()
				.orElseThrow(() -> {
					log.warn("적절한 기온 등급을 찾지 못했습니다. 기온: {}, 시간: {}", weatherTemp, LocalDateTime.now());
					return new IllegalArgumentException();
				});
		}
	}
}
