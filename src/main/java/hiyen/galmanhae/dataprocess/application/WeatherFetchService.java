package hiyen.galmanhae.dataprocess.application;

import hiyen.galmanhae.data.domain.Weather;
import hiyen.galmanhae.dataprocess.client.WeatherClient;
import hiyen.galmanhae.dataprocess.client.response.WeatherResponse;
import hiyen.galmanhae.dataprocess.exception.DataProcessUncheckedException.FailFetchAPIUncheckedException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherFetchService {

	private final WeatherClient weatherClient;

	public Weather fetch(final int weatherX, final int weatherY) {
		final LocalDateTime now = LocalDateTime.now();
		final WeatherResponse response;
		try {
			response = weatherClient.fetch(
				baseDate(now), WeatherBaseTime.of(now).getBaseTime(), String.valueOf(weatherX), String.valueOf(weatherY));
		} catch (Exception e) {
			throw new FailFetchAPIUncheckedException(e);
		}
		return Weather.of(Double.parseDouble(response.getTemperature()), Double.parseDouble(response.getRainingProbability()));
	}

	private String baseDate(final LocalDateTime now) {
		return now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	}
}
