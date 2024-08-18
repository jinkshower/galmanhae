package hiyen.galmanhae.dataprocess.application;

import hiyen.galmanhae.dataprocess.client.WeatherClient;
import hiyen.galmanhae.dataprocess.client.response.WeatherResponse;
import hiyen.galmanhae.dataprocess.exception.DataProcessCheckedException.FailFetchAPIException;
import hiyen.galmanhae.dataprocess.exception.DataProcessUncheckedException.FailFetchAPIUncheckedException;
import hiyen.galmanhae.place.domain.vo.Weather;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherService {

	private final WeatherClient weatherClient;

	public Weather fetch(final String latitude, final String longitude) {
		final LocalDateTime now = LocalDateTime.now();
		final WeatherResponse response;
		try {
			response = weatherClient.fetch(baseDate(now), WeatherBaseTime.of(now).getBaseTime(), latitude, longitude);
		} catch (FailFetchAPIException e) {
			throw new FailFetchAPIUncheckedException(e);
		}
		return Weather.of(Double.valueOf(response.getTemperature()), Double.valueOf(response.getRainingProbability()));
	}

	private String baseDate(final LocalDateTime now) {
		return now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	}
}
