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
		final WeatherResponse response;
		try {
			response = weatherClient.fetch(baseDate(), calculateBaseTime(), latitude, longitude);
		} catch (FailFetchAPIException e) {
			throw new FailFetchAPIUncheckedException(e);
		}
		return Weather.of(Double.valueOf(response.getTemperature()), Double.valueOf(response.getRainingProbability()));
	}

	private String baseDate() {
		final LocalDateTime now = LocalDateTime.now();
		return now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	}

	private String calculateBaseTime() {
		//TODO 단기예보가 갱신 - 0500 부터 세시간 씩
		// 이 중 현재시간과 가장 가까운 시간을 계산해야함. 현재는 05시로 고정
		return "0500";
	}
}
