package hiyen.galmanhae.dataprocess.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WeatherBaseTime {
	BASE_TIME_2300("2300", time -> isBefore(time, 2)),
	BASE_TIME_0200("0200", time -> isBefore(time, 5)),
	BASE_TIME_0500("0500", time -> isBefore(time, 8)),
	BASE_TIME_0800("0800", time -> isBefore(time, 11)),
	BASE_TIME_1100("1100", time -> isBefore(time, 14)),
	BASE_TIME_1400("1400", time -> isBefore(time, 17)),
	BASE_TIME_1700("1700", time -> isBefore(time, 20)),
	BASE_TIME_2000("2000", time -> isBefore(time, 23));

	private final String baseTime;
	private final Predicate<LocalDateTime> condition;

	public static WeatherBaseTime of(final LocalDateTime time) {
		return Arrays.stream(values())
			.filter(baseTime -> baseTime.condition.test(time))
			.findFirst()
			.orElse(BASE_TIME_2300); // 23시 10분 이후는 23시로 설정
	}

	/**
	 * 날씨 API는 BaseTime에서 10분 이후 부터 제공한다.
	 */
	private static boolean isBefore(final LocalDateTime time, final int hour) {
		return time.isBefore(LocalDateTime.of(time.toLocalDate(), time.toLocalTime().withHour(hour).withMinute(10)));
	}
}
