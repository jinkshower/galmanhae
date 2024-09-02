package hiyen.galmanhae.data.repository.congestion;

import hiyen.galmanhae.data.domain.Congestion;
import hiyen.galmanhae.data.domain.WeatherAndCongestion;
import hiyen.galmanhae.data.entity.CongestionEntity;
import hiyen.galmanhae.data.repository.CongestionRepository;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CongestionRepositoryImpl implements CongestionRepository {

	private final CongestionJpaRepository congestionJpaRepository;
	private final JdbcTemplate jdbcTemplate;

	public CongestionRepositoryImpl(final CongestionJpaRepository congestionJpaRepository, final DataSource dataSource) {
		this.congestionJpaRepository = congestionJpaRepository;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void saveAll(final List<WeatherAndCongestion> weatherAndCongestions) {
		final List<CongestionEntity> congestionEntities = toEntities(weatherAndCongestions);

		final String sql = """
			INSERT INTO congestion (place_id, current_people, congestion_indicator, created_at, updated_at)
			VALUES (?, ?, ?, now(), now())
			""";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				final CongestionEntity congestion = congestionEntities.get(i);
				ps.setLong(1, congestion.getPlaceId());
				ps.setInt(2, congestion.getCurrentPeople());
				ps.setString(3, congestion.getCongestionIndicator());
			}

			@Override
			public int getBatchSize() {
				return congestionEntities.size();
			}
		});
	}

	@Override
	public Congestion findMostRecentByPlaceId(final Long placeId) {
		return congestionJpaRepository.findMostRecentByPlaceId(placeId)
			.map(CongestionEntity::toDomain)
			.orElse(Congestion.of(-1, "NONE"));
	}

	@Override
	public long count() {
		return congestionJpaRepository.count();
	}

	private static List<CongestionEntity> toEntities(final List<WeatherAndCongestion> weatherAndCongestions) {
		return weatherAndCongestions.stream()
			.map(wc -> CongestionEntity.toEntity(wc.congestion(), wc.placeId()))
			.toList();
	}
}
