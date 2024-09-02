package hiyen.galmanhae.data.repository.weather;

import hiyen.galmanhae.data.domain.Weather;
import hiyen.galmanhae.data.domain.WeatherAndCongestion;
import hiyen.galmanhae.data.entity.WeatherEntity;
import hiyen.galmanhae.data.repository.WeatherRepository;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WeatherRepositoryImpl implements WeatherRepository {

	private final WeatherJpaRepository weatherJpaRepository;
	private final JdbcTemplate jdbcTemplate;

	public WeatherRepositoryImpl(final WeatherJpaRepository weatherJpaRepository, final DataSource dataSource) {
		this.weatherJpaRepository = weatherJpaRepository;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void saveAll(final List<WeatherAndCongestion> weatherAndCongestions) {
		final List<WeatherEntity> weatherEntities = toEntities(weatherAndCongestions);

		final String sql = """
			INSERT INTO weather (place_id, temperature, raining_probability, created_at, updated_at)
			VALUES (?, ?, ?, now(), now())
			""";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				final WeatherEntity weather = weatherEntities.get(i);
				ps.setLong(1, weather.getPlaceId());
				ps.setDouble(2, weather.getTemperature());
				ps.setDouble(3, weather.getRainingProbability());
			}

			@Override
			public int getBatchSize() {
				return weatherEntities.size();
			}
		});
	}

	@Override
	public Weather findMostRecentByPlaceId(final Long placeId) {
		return weatherJpaRepository.findMostRecentByPlaceId(placeId)
			.map(WeatherEntity::toDomain)
			.orElse(Weather.of(-1, -1));
	}

	@Override
	public long count() {
		return weatherJpaRepository.count();
	}

	private static List<WeatherEntity> toEntities(final List<WeatherAndCongestion> weatherAndCongestions) {
		return weatherAndCongestions.stream()
			.map(wc -> WeatherEntity.toEntity(wc.weather(), wc.placeId()))
			.toList();
	}
}
