package hiyen.galmanhae.data.repository.place;

import hiyen.galmanhae.data.entity.PlaceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlaceJpaRepository extends JpaRepository<PlaceEntity, Long> {

	@Query("""
		SELECT p FROM PlaceEntity p
		WHERE p.version = (
		    SELECT MAX(p2.version) 
		    FROM PlaceEntity p2
		)
		""")
	List<PlaceEntity> findAll();

	@Query("""
		SELECT p FROM PlaceEntity p 
		WHERE p.name LIKE %:keyword%
		 	 	""")
	List<PlaceEntity> search(String keyword);
}
