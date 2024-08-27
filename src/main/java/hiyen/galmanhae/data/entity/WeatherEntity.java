package hiyen.galmanhae.data.entity;

import hiyen.galmanhae.data.domain.Weather;
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
@Table(name = "weather")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class WeatherEntity extends BaseEntity {

	@EmbeddedId
	private TimeAndPlace timeAndPlace;

	private double temperature;

	private double rainingProbability;

	public static WeatherEntity toEntity(final Weather weather, Long placeId) {
		return new WeatherEntity(
			new TimeAndPlace(LocalDateTime.now(), placeId),
			weather.temperature(),
			weather.rainingProbability()
		);
	}

	public static Weather toDomain(final WeatherEntity entity) {
		return Weather.of(entity.getTemperature(), entity.getRainingProbability());
	}
}
