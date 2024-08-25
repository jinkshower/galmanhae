package hiyen.galmanhae.place.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hiyen.galmanhae.place.domain.place.Congestion;
import hiyen.galmanhae.place.domain.place.Congestion.CongestionLevel;
import hiyen.galmanhae.place.domain.place.Place;
import hiyen.galmanhae.place.domain.place.Place.GoOutLevel;
import hiyen.galmanhae.place.domain.place.Weather;
import hiyen.galmanhae.place.domain.place.WeatherLevel;
import hiyen.galmanhae.place.domain.place.WeatherLevel.WeatherRainingGrade;
import hiyen.galmanhae.place.domain.place.WeatherLevel.WeatherTempGrade;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Table(name = "place")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class PlaceEntity extends BaseEntity {

	@Id
	@Column(name = "place_id")
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Getter
	@Column(name = "place_name")
	private String name;

	@Embedded
	private WeatherEntity weather;

	@Embedded
	private CongestionEntity congestion;

	@Column(name = "go_out_level")
	@Enumerated(value = EnumType.STRING)
	private GoOutLevel goOutLevel;

	@Builder
	public PlaceEntity(
		final String name,
		final WeatherEntity weather,
		final CongestionEntity congestion,
		final GoOutLevel goOutLevel) {
		this(null, name, weather, congestion, goOutLevel);
	}

	public static PlaceEntity from(final Place place) {
		return PlaceEntity.builder()
			.name(place.name())
			.weather(WeatherEntity.from(place.weather()))
			.congestion(CongestionEntity.from(place.congestion()))
			.goOutLevel(place.goOutLevel())
			.build();
	}

	public static Place toPlace(final PlaceEntity entity) {
		return new Place(
			entity.getName(),
			WeatherEntity.toWeather(entity.getWeather()),
			CongestionEntity.toCongestion(entity.getCongestion()),
			entity.getGoOutLevel());
	}

	@Embeddable
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	@Getter
	@ToString
	public static class WeatherEntity {

		@Column(name = "weather_temp")
		private Double weatherTemp;

		@Column(name = "weather_raining")
		private Double weatherRaining;

		@Embedded
		private WeatherLevelEntity weatherLevel;

		public static WeatherEntity from(final Weather weather) {
			return new WeatherEntity(
				weather.weatherTemp(),
				weather.weatherRaining(),
				WeatherLevelEntity.from(weather.weatherLevel())
			);
		}

		public static Weather toWeather(final WeatherEntity entity) {
			return new Weather(
				entity.getWeatherTemp(),
				entity.getWeatherRaining(),
				WeatherLevelEntity.toWeatherLevel(entity.getWeatherLevel())
			);
		}

		@Embeddable
		@NoArgsConstructor(access = AccessLevel.PROTECTED)
		@AllArgsConstructor
		@Getter
		@ToString
		public static class WeatherLevelEntity {

			@Column(name = "weather_raining_grade")
			@Enumerated(EnumType.STRING)
			private WeatherRainingGrade weatherRainingGrade;

			@Column(name = "weather_temp_grade")
			@Enumerated(EnumType.STRING)
			private WeatherTempGrade weatherTempGrade;

			@Column(name = "weather_score")
			private Integer weatherScore;

			public static WeatherLevelEntity from(final WeatherLevel weatherLevel) {
				return new WeatherLevelEntity(
					weatherLevel.weatherRainingGrade(),
					weatherLevel.weatherTempGrade(),
					weatherLevel.weatherScore()
				);
			}

			public static WeatherLevel toWeatherLevel(final WeatherLevelEntity entity) {
				return new WeatherLevel(
					entity.getWeatherRainingGrade(),
					entity.getWeatherTempGrade(),
					entity.getWeatherScore()
				);
			}
		}
	}

	@Embeddable
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	@Getter
	@ToString
	public static class CongestionEntity {

		@Column(name = "congestion_people")
		private Integer congestionPeople;

		@Column(name = "congestion_level")
		@Enumerated(EnumType.STRING)
		private CongestionLevel congestionLevel;

		public static CongestionEntity from(final Congestion congestion) {
			return new CongestionEntity(
				congestion.congestionPeople(),
				congestion.congestionLevel()
			);
		}

		public static Congestion toCongestion(final CongestionEntity entity) {
			return new Congestion(
				entity.getCongestionPeople(),
				entity.getCongestionLevel()
			);
		}
	}
}

