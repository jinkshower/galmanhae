package hiyen.galmanhae.dataprocess.client;

import hiyen.galmanhae.dataprocess.client.response.CongestionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
	name = "congestionClient",
	url = "${client.congestion.url}",
	configuration = FeignConfig.class
)
public interface CongestionClient {

	@GetMapping("/${client.congestion.service-key}/json/citydata_ppltn/1/1/{area-code}")
	CongestionResponse fetch(@PathVariable("area-code") String areaCode);
}
