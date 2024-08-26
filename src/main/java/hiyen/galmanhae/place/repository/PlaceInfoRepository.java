package hiyen.galmanhae.place.repository;

import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo;
import java.util.List;

public interface PlaceInfoRepository {

	List<PlaceInfo> saveAll(List<PlaceInfo> placeInfos);

	List<PlaceInfo> findAll();

	long count();

	PlaceInfo save(PlaceInfo place);

	List<PlaceInfo> search(String keyword);
}
