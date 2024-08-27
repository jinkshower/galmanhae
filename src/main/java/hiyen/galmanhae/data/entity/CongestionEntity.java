package hiyen.galmanhae.data.entity;

import hiyen.galmanhae.data.domain.Congestion;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "congestion")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class CongestionEntity extends BaseEntity {

	@EmbeddedId
	private TimeAndPlace timeAndPlace;

	@Column(name = "current_people")
	private int currentPeople;

	@Column(name = "congestion_indicator", length = 10)
	private String congestionIndicator;

	public static CongestionEntity toEntity(final Congestion congestion, Long placeId) {
		return new CongestionEntity(
			new TimeAndPlace(LocalDateTime.now(), placeId),
			congestion.currentPeople(),
			congestion.congestionIndicator()
		);
	}

	public static Congestion toDomain(final CongestionEntity entity) {
		return Congestion.of(entity.getCurrentPeople(), entity.getCongestionIndicator());
	}
}
