package hiyen.galmanhae.place.repository.place;

import hiyen.galmanhae.place.domain.place.Place;
import hiyen.galmanhae.place.entity.PlaceEntity;
import hiyen.galmanhae.place.exception.PlaceException.NotFoundPlaceException;
import hiyen.galmanhae.place.repository.PlaceRepository;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PlaceRepositoryImpl implements PlaceRepository {

	private final PlaceJpaRepository placeJpaRepository;
	private final JdbcTemplate jdbcTemplate;

	public PlaceRepositoryImpl(final PlaceJpaRepository placeJpaRepository, final DataSource dataSource) {
		this.placeJpaRepository = placeJpaRepository;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Place> saveAll(final List<Place> places) {
		final List<PlaceEntity> entities = toEntities(places);

		final String sql = """
			INSERT INTO place (place_name, weather_temp, weather_raining, weather_raining_grade, weather_temp_grade, 
			weather_score, congestion_people, congestion_level, go_out_level, created_at, updated_at) 
			VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, now(), now())
			""";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				final PlaceEntity place = entities.get(i);
				ps.setString(1, place.getName());
				ps.setDouble(2, place.getWeather().getWeatherTemp());
				ps.setDouble(3, place.getWeather().getWeatherRaining());
				ps.setString(4, place.getWeather().getWeatherLevel().getWeatherRainingGrade().name());
				ps.setString(5, place.getWeather().getWeatherLevel().getWeatherTempGrade().name());
				ps.setInt(6, place.getWeather().getWeatherLevel().getWeatherScore());
				ps.setInt(7, place.getCongestion().getCongestionPeople());
				ps.setString(8, place.getCongestion().getCongestionLevel().name());
				ps.setString(9, place.getGoOutLevel().name());
			}

			@Override
			public int getBatchSize() {
				return entities.size();
			}
		});

		return places;
	}

	@Override
	public List<Place> findAll() {
		return placeJpaRepository.findAll().stream()
			.map(PlaceEntity::toPlace)
			.toList();
	}

	@Override
	public long count() {
		return placeJpaRepository.count();
	}

	@Override
	public Place findByName(final String placeName) {
		final PlaceEntity found = placeJpaRepository.findByName(placeName)
			.orElseThrow(NotFoundPlaceException::new);
		return PlaceEntity.toPlace(found);
	}

	private static List<PlaceEntity> toEntities(final List<Place> places) {
		return places.stream()
			.map(PlaceEntity::from)
			.toList();
	}
}
