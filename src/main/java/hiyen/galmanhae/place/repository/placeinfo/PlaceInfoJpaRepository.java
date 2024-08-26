package hiyen.galmanhae.place.repository.placeinfo;

import hiyen.galmanhae.place.entity.PlaceInfoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlaceInfoJpaRepository extends JpaRepository<PlaceInfoEntity, Long> {

	@Query("Select p from PlaceInfoEntity p where p.areaInfo.areaName like %:keyword%")
	List<PlaceInfoEntity> search(String keyword);
}
