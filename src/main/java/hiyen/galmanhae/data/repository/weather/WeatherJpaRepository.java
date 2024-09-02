package hiyen.galmanhae.data.repository.weather;

import hiyen.galmanhae.data.entity.WeatherEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WeatherJpaRepository extends JpaRepository<WeatherEntity, Long> {

	@Query(value = """
		SELECT * FROM weather w 
		WHERE w.place_id = :placeId 
		ORDER BY w.created_at DESC 
		LIMIT 1
		""", nativeQuery = true)
	Optional<WeatherEntity> findMostRecentByPlaceId(@Param("placeId") Long placeId);

	@Query(value = """
		SELECT *
		FROM weather w1
		WHERE w1.created_at = (
		    SELECT MAX(w2.created_at)
		    FROM weather w2
		    WHERE w2.place_id = w1.place_id
		) AND w1.place_id IN :placeIds
		""", nativeQuery = true)
	List<WeatherEntity> findMostRecentByPlaceIds(@Param("placeIds") List<Long> placeIds);
}
