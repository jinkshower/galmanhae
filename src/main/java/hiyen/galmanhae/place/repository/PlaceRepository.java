package hiyen.galmanhae.place.repository;

import hiyen.galmanhae.place.domain.place.Place;
import java.util.List;

public interface PlaceRepository {

	List<Place> saveAll(List<Place> placeEntities);

	List<Place> findAll();

	long count();
}
