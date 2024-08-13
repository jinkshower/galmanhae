package hiyen.galmanhae.dataprocess.client;

import hiyen.galmanhae.dataprocess.dto.WeatherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weather-api", url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0")
public interface WeatherClient {

	@GetMapping("/getVilageFcst?serviceKey=haOR%2F%2BX0ZiSttZyEP6tIGmnLnbOzCvyOL8RCjZOmg23EGAGwEps1K22f%2FiPk8F75oVRxarkMET6T1d1dLoGXTw%3D%3D&numOfRows=10&pageNo=1&dataType=JSON")
	WeatherResponse fetch(
		@RequestParam("base_date") String baseDate,
		@RequestParam("base_time") String baseTime,
		@RequestParam("nx") String nx,
		@RequestParam("ny") String ny
	);
}
