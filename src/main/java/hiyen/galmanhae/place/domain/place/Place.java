package hiyen.galmanhae.place.domain.place;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hiyen.galmanhae.place.domain.place.Congestion;
import hiyen.galmanhae.place.domain.place.Location;
import hiyen.galmanhae.place.domain.place.Weather;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public class Place {

	@Id
	@Column(name = "place_id")
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "place_name")
	private String name;

	@Embedded
	private Location location;

	@Embedded
	private Weather weather;

	@Embedded
	private Congestion congestion;

	@Column(name = "go_out_level")
	@Enumerated(value = EnumType.STRING)
	private GoOutLevel goOutLevel;

	//TODO 이후 Jpa Auditing or Clock 인터페이스 -> 구현 후 주입으로 변경예정
	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();

	public Place(
		final Long id,
		final String name,
		final Location location,
		final Weather weather,
		final Congestion congestion,
		final GoOutLevel goOutLevel) {

		if (!StringUtils.hasText(name)) {
			log.warn("장소 이름이 없어요. 시간: {}", LocalDateTime.now());
			throw new IllegalArgumentException();
		}

		this.id = id;
		this.name = name;
		this.location = location;
		this.weather = weather;
		this.congestion = congestion;
		this.goOutLevel = goOutLevel;
	}

	@Builder
	public Place(
		final String name,
		final Location location,
		final Weather weather,
		final Congestion congestion,
		final GoOutLevel goOutLevel) {
		this(null, name, location, weather, congestion, goOutLevel);
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
