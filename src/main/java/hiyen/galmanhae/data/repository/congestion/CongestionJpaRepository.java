package hiyen.galmanhae.data.repository.congestion;

import hiyen.galmanhae.data.entity.CongestionEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CongestionJpaRepository extends JpaRepository<CongestionEntity, Long> {

	@Query(value = "SELECT * FROM congestion c WHERE c.place_id = :placeId ORDER BY c.measured_at DESC LIMIT 1", nativeQuery = true)
	Optional<CongestionEntity> findMostRecentByPlaceId(@Param("placeId") Long placeId);
}
