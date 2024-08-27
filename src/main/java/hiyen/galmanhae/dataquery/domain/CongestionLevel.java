package hiyen.galmanhae.dataquery.domain;


import hiyen.galmanhae.data.domain.Congestion;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum CongestionLevel {
	RELAXED("여유로워요", 50, "여유"),
	NORMAL("보통이에요", 30, "보통"),
	SLIGHTLY_CROWDED("약간 붐벼요", 15, "약간 붐빔"),
	CROWDED("사람 많아요", 5, "붐빔"),
	NONE(" ", 0, "정보 없음");

	private final String description;
	private final int score;
	private final String congestionIndicator;

	public static CongestionLevel of(final Congestion congestion) {
		return Arrays.stream(values())
			.filter(
				level -> level.congestionIndicator.equals(congestion.congestionIndicator()))
			.findFirst()
			.orElse(NONE);
	}
}
