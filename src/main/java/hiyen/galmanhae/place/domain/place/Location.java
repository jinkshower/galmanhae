package hiyen.galmanhae.place.domain.place;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Location {

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;

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
