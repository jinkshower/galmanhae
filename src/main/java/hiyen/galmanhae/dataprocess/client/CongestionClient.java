package hiyen.galmanhae.dataprocess.client;

import hiyen.galmanhae.dataprocess.client.CongestionClient.CongestionClientFallbackFactory;
import hiyen.galmanhae.dataprocess.client.response.CongestionResponse;
import hiyen.galmanhae.dataprocess.exception.DataProcessCheckedException.FailFetchAPIException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
	name = "congestionClient",
	url = "${client.congestion.url}",
	fallbackFactory = CongestionClientFallbackFactory.class
)
@CircuitBreaker(name = "circuit")
public interface CongestionClient {

	@GetMapping("/${client.congestion.service-key}/json/citydata_ppltn/1/1/{area-code}")
	CongestionResponse fetch(@PathVariable("area-code") String areaCode) throws FailFetchAPIException;

	//TODO 예외 처리 방법 재고려 필요
	@Slf4j
	@Component
	class CongestionClientFallbackFactory implements FallbackFactory<CongestionClient> {

		@Override
		public CongestionClient create(final Throwable cause) {
			return areaCode -> {
				log.warn("Client에서 정보를 받지 못했습니다. areaCode: {}", areaCode);
				throw new FailFetchAPIException(cause);
			};
		}
	}
}
