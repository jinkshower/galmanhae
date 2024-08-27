package hiyen.galmanhae.place.repository.placeinfo;

import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo;
import hiyen.galmanhae.place.entity.PlaceInfoEntity;
import hiyen.galmanhae.place.repository.PlaceInfoRepository;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PlaceInfoRepositoryImpl implements PlaceInfoRepository {

	private final PlaceInfoJpaRepository placeInfoJpaRepository;
	private final JdbcTemplate jdbcTemplate;

	public PlaceInfoRepositoryImpl(final PlaceInfoJpaRepository placeInfoJpaRepository, final DataSource dataSource) {
		this.placeInfoJpaRepository = placeInfoJpaRepository;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<PlaceInfo> saveAll(final List<PlaceInfo> placeInfos) {
		final List<PlaceInfoEntity> entities = toEntities(placeInfos);

		final String sql = """
			INSERT INTO place_info (area_code, area_name, latitude, longitude, weather_x, weather_y)
			VALUES (?, ?, ?, ?, ?, ?)
			""";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(final PreparedStatement ps, final int i) throws SQLException {
				final PlaceInfoEntity entity = entities.get(i);
				ps.setString(1, entity.getAreaInfo().getAreaCode());
				ps.setString(2, entity.getAreaInfo().getAreaName());
				ps.setString(3, entity.getLocationInfo().getLatitude());
				ps.setString(4, entity.getLocationInfo().getLongitude());
				ps.setString(5, entity.getWeatherInfo().getWeatherX());
				ps.setString(6, entity.getWeatherInfo().getWeatherY());
			}

			@Override
			public int getBatchSize() {
				return entities.size();
			}
		});

		return placeInfos;
	}

	@Override
	public List<PlaceInfo> findAll() {
		return placeInfoJpaRepository.findAll().stream()
			.map(PlaceInfoEntity::toPlaceInfo)
			.toList();
	}

	@Override
	public long count() {
		return placeInfoJpaRepository.count();
	}

	@Override
	public PlaceInfo save(final PlaceInfo placeInfo) {
		final PlaceInfoEntity save = placeInfoJpaRepository.save(PlaceInfoEntity.from(placeInfo));
		return PlaceInfoEntity.toPlaceInfo(save);
	}

	@Override
	public List<PlaceInfo> search(final String keyword) {
		final List<PlaceInfoEntity> found = placeInfoJpaRepository.search(keyword);
		return found.stream()
			.map(PlaceInfoEntity::toPlaceInfo)
			.toList();
	}

	@Override
	public void deleteAll() {
		placeInfoJpaRepository.deleteAll();
	}

	private static List<PlaceInfoEntity> toEntities(final List<PlaceInfo> placeInfos) {
		return placeInfos.stream()
			.map(PlaceInfoEntity::from)
			.toList();
	}
}
