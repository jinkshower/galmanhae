package hiyen.galmanhae.dataprocess.client;

import hiyen.galmanhae.dataprocess.dto.CongestionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "congestion-api", url = "http://openapi.seoul.go.kr:8088")
public interface CongestionClient {

	@GetMapping("/7a6a64746161736b3638785a6a5969/json/citydata_ppltn/1/1/{area-code}")
	CongestionDTO fetch(@PathVariable("area-code") String areaCode);
}
