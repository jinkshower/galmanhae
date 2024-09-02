package hiyen.galmanhae.data.entity;

import hiyen.galmanhae.data.domain.Weather;
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
	name = "weather"
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class WeatherEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long placeId;

	private double temperature;

	private double rainingProbability;

	public WeatherEntity(final Long placeId, final double temperature, final double rainingProbability) {
		this(null, placeId, temperature, rainingProbability);
	}

	public static WeatherEntity toEntity(final Weather weather, Long placeId) {
		return new WeatherEntity(
			placeId,
			weather.temperature(),
			weather.rainingProbability()
		);
	}

	public static Weather toDomain(final WeatherEntity entity) {
		return Weather.of(entity.getTemperature(), entity.getRainingProbability());
	}
}
