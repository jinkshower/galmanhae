package hiyen.galmanhae.dataprocess.client;

import hiyen.galmanhae.dataprocess.client.WeatherClient.WeatherClientFallbackFactory;
import hiyen.galmanhae.dataprocess.client.response.WeatherResponse;
import hiyen.galmanhae.dataprocess.exception.DataProcessCheckedException.FailFetchAPIException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
	name = "weatherClient",
	url = "${client.weather.url}",
	fallbackFactory = WeatherClientFallbackFactory.class
)
@CircuitBreaker(name = "circuit")
public interface WeatherClient {

	@GetMapping("/getVilageFcst?serviceKey=${client.weather.service-key}&numOfRows=10&pageNo=1&dataType=JSON")
	WeatherResponse fetch(
		@RequestParam("base_date") String baseDate,
		@RequestParam("base_time") String baseTime,
		@RequestParam("nx") String nx,
		@RequestParam("ny") String ny
	) throws FailFetchAPIException;

	//TODO 예외 처리 방법 재고려 필요
	@Slf4j
	@Component
	class WeatherClientFallbackFactory implements FallbackFactory<WeatherClient> {

		@Override
		public WeatherClient create(final Throwable cause) {
			return (baseDate, baseTime, nx, ny) -> {
				log.warn("Client에서 정보를 받지 못했습니다. baseDate: {}, baseTime: {}, nx: {}, ny: {}", baseDate, baseTime, nx, ny);
				throw new FailFetchAPIException(cause);
			};
		}
	}
}
