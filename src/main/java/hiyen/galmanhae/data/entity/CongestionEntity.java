package hiyen.galmanhae.data.entity;

import hiyen.galmanhae.data.domain.Congestion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(
	name = "congestion"
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class CongestionEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "place_id")
	private Long placeId;

	@Column(name = "current_people")
	private int currentPeople;

	@Column(name = "congestion_indicator", length = 10)
	private String congestionIndicator;

	public CongestionEntity(final Long placeId, final int currentPeople, final String congestionIndicator) {
		this(null, placeId, currentPeople, congestionIndicator);
	}

	public static CongestionEntity toEntity(final Congestion congestion, Long placeId) {
		return new CongestionEntity(
			placeId,
			congestion.currentPeople(),
			congestion.congestionIndicator()
		);
	}

	public static Congestion toDomain(final CongestionEntity entity) {
		return Congestion.of(entity.getCurrentPeople(), entity.getCongestionIndicator());
	}
}
