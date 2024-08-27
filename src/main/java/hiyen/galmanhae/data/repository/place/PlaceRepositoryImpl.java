package hiyen.galmanhae.data.repository.place;

import hiyen.galmanhae.data.domain.Place;
import hiyen.galmanhae.data.entity.PlaceEntity;
import hiyen.galmanhae.data.exception.DataException.NotFoundPlaceException;
import hiyen.galmanhae.data.repository.PlaceRepository;
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
	public List<Place> findAll() {
		return placeJpaRepository.findAll().stream()
			.map(PlaceEntity::toDomain)
			.toList();
	}

	@Override
	public List<Place> saveAll(final List<Place> places) {
		final List<PlaceEntity> entities = toEntities(places);

		final String sql = """
			INSERT INTO place (name, code, latitude, longitude, weatherX, weatherY, created_at, updated_at)
			VALUES (?, ?, ?, ?, ?, ?, now(), now())
			""";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				final PlaceEntity place = entities.get(i);
				ps.setString(1, place.getName());
				ps.setString(2, place.getCode());
				ps.setDouble(3, place.getLatitude());
				ps.setDouble(4, place.getLongitude());
				ps.setInt(5, place.getWeatherX());
				ps.setInt(6, place.getWeatherY());
			}

			@Override
			public int getBatchSize() {
				return entities.size();
			}
		});

		return places;
	}

	@Override
	public void deleteAll() {
		placeJpaRepository.deleteAll();
	}

	@Override
	public Place save(final Place place) {
		final PlaceEntity saved = placeJpaRepository.save(PlaceEntity.toEntity(place));
		return PlaceEntity.toDomain(saved);
	}

	@Override
	public long count() {
		return placeJpaRepository.count();
	}

	@Override
	public List<Place> search(final String keyword) {
		return placeJpaRepository.search(keyword).stream()
			.map(PlaceEntity::toDomain)
			.toList();
	}

	@Override
	public Place findById(final Long placeId) {
		return placeJpaRepository.findById(placeId)
			.map(PlaceEntity::toDomain)
			.orElseThrow(() -> new NotFoundPlaceException(placeId));
	}

	private static List<PlaceEntity> toEntities(final List<Place> places) {
		return places.stream()
			.map(PlaceEntity::toEntity)
			.toList();
	}
}

