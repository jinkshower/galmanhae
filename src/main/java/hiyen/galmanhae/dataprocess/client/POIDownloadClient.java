package hiyen.galmanhae.dataprocess.client;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(
	name = "poiDownloadClient",
	url = "https://datafile.seoul.go.kr"
)
public interface POIDownloadClient {

	@PostMapping("/bigfile/iot/inf/nio_download.do?infId=OA-21778&seq=4&infSeq=2&useCache=false")
	Response fetch();
}
