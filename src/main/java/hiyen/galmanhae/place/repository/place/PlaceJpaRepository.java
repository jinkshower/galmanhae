package hiyen.galmanhae.place.repository.place;

import hiyen.galmanhae.place.entity.PlaceEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceJpaRepository extends JpaRepository<PlaceEntity, Long> {

	@Query("SELECT p FROM PlaceEntity p WHERE p.name = :placeName ORDER BY p.createdAt DESC")
	Optional<PlaceEntity> findByName(@Param("placeName") String placeName);
}
