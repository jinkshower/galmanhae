package hiyen.galmanhae.place.domain.place;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record Location(
	Double latitude,
	Double longitude
) {

	public static Location of(final Double latitude, final Double longitude) {
		if (Objects.isNull(latitude)) {
			log.warn("위도가 없어요. 시간: {}", LocalDateTime.now());
			throw new IllegalArgumentException();
		}

		if (Objects.isNull(longitude)) {
			log.warn("경도가 없어요. 시간: {}", LocalDateTime.now());
			throw new IllegalArgumentException();
		}

		return new Location(latitude, longitude);
	}
}
