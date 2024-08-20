package hiyen.galmanhae.place.domain.place;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.function.Predicate;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public record Place(
	String name,
	Location location,
	Weather weather,
	Congestion congestion,
	GoOutLevel goOutLevel

) {
	@Builder
	public Place {
		if (!StringUtils.hasText(name)) {
			log.warn("장소 이름이 없어요. 시간: {}", LocalDateTime.now());
			throw new IllegalArgumentException();
		}
	}

	@Getter
	@RequiredArgsConstructor
	public enum GoOutLevel {
		LOW("다른 장소를 찾아 볼까요?", score -> score <= 40),
		MEDIUM("무난해요", score -> score <= 70),
		HIGH("외출하기 좋아요!", score -> score > 70);

		private final String description;

		private final Predicate<Integer> condition;

		public static GoOutLevel of(final Weather weather, final Congestion congestion) {
			return Arrays.stream(values())
				.filter(level -> level.condition.test(weather.getScore() + congestion.getScore()))
				.findFirst()
				.orElseThrow(() -> {
					log.warn("적절한 외출 등급을 찾지 못했습니다. 날씨: {}, 혼잡도: {} 시간: {}", weather, congestion, LocalDateTime.now());
					return new IllegalArgumentException();
				});
		}
	}
}
