package hiyen.galmanhae.place.domain.place;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.util.StringUtils;

@Slf4j
public record Congestion(
	Integer congestionPeople,
	CongestionLevel congestionLevel

) {

	public static Congestion of(final Integer congestionPeople, final String congestionIndicator) {
		if (Objects.isNull(congestionPeople)) {
			log.warn("실시간 인구지표가 없어요. 시간 : {}", LocalDateTime.now());
			throw new IllegalArgumentException();
		}
		if (StringUtils.isEmpty(congestionIndicator)) {
			log.warn("실시간 혼잡도 지표가 없어요. 시간 : {}", LocalDateTime.now());
			throw new IllegalArgumentException();
		}

		return new Congestion(congestionPeople, CongestionLevel.of(congestionIndicator));
	}

	public String getDescription() {
		return congestionLevel.getDescription();
	}

	public int getScore() {
		return congestionLevel.getScore();
	}

	@Getter
	@RequiredArgsConstructor
	public enum CongestionLevel {
		RELAXED("여유로워요", 50, "여유"),
		NORMAL("보통이에요", 30, "보통"),
		SLIGHTLY_CROWDED("약간 붐벼요", 15, "약간 붐빔"),
		CROWDED("사람 많아요", 5, "붐빔");

		private final String description;
		private final int score;
		private final String congestionIndicator;

		public static CongestionLevel of(final String congestionIndicator) {
			return Arrays.stream(values())
				.filter(
					level -> level.congestionIndicator.equals(congestionIndicator))
				.findFirst()
				.orElseThrow(() -> {
					log.warn("적절한 혼잡도 등급을 찾지 못했습니다. 혼잡도: {}, 시간: {}", congestionIndicator, LocalDateTime.now());
					return new IllegalArgumentException();
				});
		}
	}
}
