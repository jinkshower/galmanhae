package hiyen.galmanhae.dataprocess;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo.AreaInfo;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo.LocationInfo;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo.WeatherInfo;
import hiyen.galmanhae.place.repository.PlaceInfoRepository;
import hiyen.galmanhae.place.repository.PlaceRepository;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

class DataProcessorIntegrationTest extends MockAPI {

	@Autowired
	private DataProcessor dataProcessor;

	@Value("${client.congestion.service-key}")
	private String congestionServiceKey;

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private PlaceInfoRepository placeInfoRepository;

	private String areaCode = "POI001";

	@BeforeEach
	void setUp() {
		final PlaceInfo placeInfo = new PlaceInfo(
			new AreaInfo(areaCode, "강남구"),
			new LocationInfo("37.1234", "127.1234"),
			new WeatherInfo("60", "127")
		);
		placeInfoRepository.save(placeInfo);
	}

	@DisplayName("외부 API를 호출하여 데이터를 DB에 저장한다")
	@Test
	void testCallCongestionClient() throws URISyntaxException, IOException {
		// Weather API Stub 설정
		setupWeatherStub(aResponse()
			.withStatus(200)
			.withHeader("Content-Type", "application/json")
			.withBody(validWeatherResponse()));

		// Congestion API Stub 설정
		setupCongestionStub(aResponse()
			.withStatus(200)
			.withHeader("Content-Type", "application/json")
			.withBody(validCongestionResponse()), areaCode);

		dataProcessor.process();

		assertThat(placeRepository.count()).isEqualTo(1);
	}

	private void setupCongestionStub(final ResponseDefinitionBuilder responseBuilder, final String areaCode) {
		stubFor(get(urlPathEqualTo("/" + congestionServiceKey + "/json/citydata_ppltn/1/1/" + areaCode))
			.willReturn(responseBuilder)
		);
	}

	private void setupWeatherStub(final ResponseDefinitionBuilder responseBuilder) {
		stubFor(get(urlPathEqualTo("/getVilageFcst"))
			.withQueryParam("serviceKey", matching(".*"))
			.withQueryParam("base_date", matching(".*"))
			.withQueryParam("base_time", matching(".*"))
			.withQueryParam("nx", matching(".*"))
			.withQueryParam("ny", matching(".*"))
			.willReturn(responseBuilder));
	}

	private String validWeatherResponse() throws URISyntaxException, IOException {
		return new String(Files.readAllBytes(Paths.get(
			getClass().getClassLoader().getResource("valid-weather-response.json").toURI()
		)));
	}

	private String validCongestionResponse() throws URISyntaxException, IOException {
		return new String(Files.readAllBytes(Paths.get(
			getClass().getClassLoader().getResource("valid-congestion-response.json").toURI()
		)));
	}
}
