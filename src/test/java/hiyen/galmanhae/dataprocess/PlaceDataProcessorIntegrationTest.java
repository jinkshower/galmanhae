package hiyen.galmanhae.dataprocess;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import hiyen.galmanhae.common.MockAPI;
import hiyen.galmanhae.data.repository.PlaceRepository;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {
	"client.placeinfo.filename=test_location"
})
class PlaceDataProcessorIntegrationTest extends MockAPI {

	@Autowired
	private PlaceDataProcessor placeDataProcessor;

	@Autowired
	private PlaceRepository placeRepository;

	@DisplayName("외부 API를 호출로 다운로드 받은 파일을 파싱하여 db에 저장한다")
	@Test
	void downloadParseSave() throws URISyntaxException, IOException {
		setupPlaceInfoStub(aResponse()
			.withStatus(200)
			.withHeader("Content-Type", "application/x-msdownload")
			.withBody(validPlaceInfoResponse()));

		placeDataProcessor.process();

		assertThat(placeRepository.count()).isEqualTo(1);
	}

	@DisplayName("외부 API 호출이 두번 이상 일때 최신의 데이터만 저장한다")
	@Test
	void downloadParseSaveTwice() throws URISyntaxException, IOException {
		setupPlaceInfoStub(aResponse()
			.withStatus(200)
			.withHeader("Content-Type", "application/x-msdownload")
			.withBody(validPlaceInfoResponse()));

		placeDataProcessor.process();
		placeDataProcessor.process();

		assertThat(placeRepository.count()).isEqualTo(1);
	}

	private void setupPlaceInfoStub(final ResponseDefinitionBuilder responseBuilder) {
		stubFor(post(urlPathEqualTo("/bigfile/iot/inf/nio_download.do"))
			.withQueryParam("infId", matching(".*"))
			.withQueryParam("seq", matching(".*"))
			.withQueryParam("infSeq", matching(".*"))
			.withQueryParam("useCache", matching(".*"))
			.willReturn(responseBuilder)
		);
	}

	private byte[] validPlaceInfoResponse() throws URISyntaxException, IOException {
		return Files.readAllBytes(Paths.get(
			getClass().getClassLoader().getResource("test_location.zip").toURI()
		));
	}
}
