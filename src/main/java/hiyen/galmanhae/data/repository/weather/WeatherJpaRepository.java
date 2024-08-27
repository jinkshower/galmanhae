package hiyen.galmanhae.data.repository.weather;

import hiyen.galmanhae.data.entity.WeatherEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WeatherJpaRepository extends JpaRepository<WeatherEntity, Long> {

	@Query(value = "SELECT * FROM weather w WHERE w.place_id = :placeId ORDER BY w.measured_at DESC LIMIT 1", nativeQuery = true)
	Optional<WeatherEntity> findMostRecentByPlaceId(@Param("placeId") Long placeId);
}
