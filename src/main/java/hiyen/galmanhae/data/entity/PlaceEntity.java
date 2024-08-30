package hiyen.galmanhae.data.entity;

import hiyen.galmanhae.data.domain.Place;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "place")
@Getter
@ToString
public class PlaceEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(length = 10, unique = true)
	private String code;

	private double latitude;

	private double longitude;

	@Column(length = 5)
	private int weatherX;

	@Column(length = 5)
	private int weatherY;

	public static Place toDomain(final PlaceEntity entity) {
		return new Place(
			entity.getId(),
			new Place.PlaceNameAndCode(entity.getName(), entity.getCode()),
			new Place.Position(entity.getLatitude(), entity.getLongitude()),
			new Place.WeatherPosition(entity.getWeatherX(), entity.getWeatherY())
		);
	}

	public static PlaceEntity toEntity(final Place place) {
		return new PlaceEntity(
			place.id(),
			place.placeNameAndCode().name(),
			place.placeNameAndCode().code(),
			place.position().latitude(),
			place.position().longitude(),
			place.weatherPosition().weatherX(),
			place.weatherPosition().weatherY()
		);
	}
}
