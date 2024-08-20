package hiyen.galmanhae.dataprocess.client;

import hiyen.galmanhae.dataprocess.client.response.WeatherResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
	name = "weatherClient",
	url = "${client.weather.url}",
	configuration = FeignConfig.class
)
@CircuitBreaker(name = "circuit")
public interface WeatherClient {

	@GetMapping("/getVilageFcst?serviceKey=${client.weather.service-key}&numOfRows=10&pageNo=1&dataType=JSON")
	WeatherResponse fetch(
		@RequestParam("base_date") String baseDate,
		@RequestParam("base_time") String baseTime,
		@RequestParam("nx") String nx,
		@RequestParam("ny") String ny
	);
}
