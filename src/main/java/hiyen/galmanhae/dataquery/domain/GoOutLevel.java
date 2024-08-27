package hiyen.galmanhae.dataquery.domain;

import java.util.Arrays;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
@RequiredArgsConstructor
public enum GoOutLevel {

	LOW("다른 장소를 찾아 볼까요?", score -> score > 0 && score <= 40),
	MEDIUM("무난해요", score -> score > 40 && score <= 70),
	HIGH("외출하기 좋아요!", score -> score > 70),
	NONE("일시적으로 외출도를 계산할 수 없어요", score -> true);

	private final String description;

	private final Predicate<Integer> condition;

	public static GoOutLevel of(final WeatherLevel weatherLevel, final CongestionLevel congestionLevel) {
		return Arrays.stream(values())
			.filter(level -> level.condition.test(weatherLevel.weatherScore() + congestionLevel.getScore()))
			.findFirst()
			.orElse(NONE);
	}
}

