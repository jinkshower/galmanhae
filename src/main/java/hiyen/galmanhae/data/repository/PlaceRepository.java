package hiyen.galmanhae.data.repository;

import hiyen.galmanhae.data.domain.Place;
import java.util.List;

public interface PlaceRepository {

	List<Place> findAll();

	List<Place> saveAll(List<Place> places);

	Place save(Place place);

	long count();

	List<Place> search(String keyword);

	Place findById(Long placeId);
}
