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
		SELECT c1.*
		FROM congestion c1
		INNER JOIN (
		    SELECT place_id, MAX(created_at) AS max_created_at
		    FROM congestion
		    WHERE place_id IN :placeIds
		    GROUP BY place_id
		) c2 ON c1.place_id = c2.place_id AND c1.created_at = c2.max_created_at
		""", nativeQuery = true)
	List<CongestionEntity> findMostRecentByPlaceIds(@Param("placeIds") List<Long> placeIds);
}
