package hiyen.galmanhae.data.repository;

import hiyen.galmanhae.data.domain.Weather;
import hiyen.galmanhae.data.domain.WeatherAndCongestion;
import java.util.List;

public interface WeatherRepository {

	void saveAll(List<WeatherAndCongestion> weatherAndCongestions);

	Weather findMostRecentByPlaceId(Long placeId);

	long count();

	List<Weather> findMostRecentByPlaceIds(List<Long> placeIds);
}
