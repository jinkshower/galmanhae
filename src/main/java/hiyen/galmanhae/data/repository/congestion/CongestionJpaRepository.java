package hiyen.galmanhae.data.repository.congestion;

import hiyen.galmanhae.data.entity.CongestionEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CongestionJpaRepository extends JpaRepository<CongestionEntity, Long> {

	@Query(value = """
		SELECT * 
		FROM congestion c 
		WHERE c.place_id = :placeId 
		ORDER BY c.created_at DESC 
		LIMIT 1
		""", nativeQuery = true)
	Optional<CongestionEntity> findMostRecentByPlaceId(@Param("placeId") Long placeId);

	@Query(value = """
		SELECT *
		FROM congestion c1
		WHERE c1.created_at = (
		    SELECT MAX(c2.created_at)
		    FROM congestion c2
		    WHERE c2.place_id = c1.place_id
		) AND c1.place_id IN :placeIds
		""", nativeQuery = true)
	List<CongestionEntity> findMostRecentByPlaceIds(List<Long> placeIds);
}
