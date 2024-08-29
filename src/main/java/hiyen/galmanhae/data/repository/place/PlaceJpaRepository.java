package hiyen.galmanhae.data.repository.place;

import hiyen.galmanhae.data.entity.PlaceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PlaceJpaRepository extends JpaRepository<PlaceEntity, Long> {

	List<PlaceEntity> findAll();

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("delete from PlaceEntity p")
	void deleteAll();

	@Query("select p from PlaceEntity p where p.name like %:keyword%")
	List<PlaceEntity> search(String keyword);
}
