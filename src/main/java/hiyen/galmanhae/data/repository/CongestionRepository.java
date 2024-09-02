package hiyen.galmanhae.data.repository;

import hiyen.galmanhae.data.domain.Congestion;
import hiyen.galmanhae.data.domain.WeatherAndCongestion;
import java.util.List;

public interface CongestionRepository {

	void saveAll(List<WeatherAndCongestion> weatherAndCongestions);

	Congestion findMostRecentByPlaceId(Long placeId);

	long count();

	List<Congestion> findMostRecentByPlaceIds(List<Long> placeIds);
}
