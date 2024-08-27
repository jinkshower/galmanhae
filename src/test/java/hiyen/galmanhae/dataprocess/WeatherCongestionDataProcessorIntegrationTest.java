package hiyen.galmanhae.dataprocess;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import hiyen.galmanhae.common.MockAPI;
import hiyen.galmanhae.data.domain.Place;
import hiyen.galmanhae.data.repository.CongestionRepository;
import hiyen.galmanhae.data.repository.PlaceRepository;
import hiyen.galmanhae.data.repository.WeatherRepository;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

class WeatherCongestionDataProcessorIntegrationTest extends MockAPI {

	@Autowired
	private WeatherCongestionDataProcessor weatherCongestionDataProcessor;

	@Value("${client.congestion.service-key}")
	private String congestionServiceKey;

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private WeatherRepository weatherRepository;

	@Autowired
	private CongestionRepository congestionRepository;

	private String areaCode = "POI001";

	@BeforeEach
	void setUp() {
		final Place place = Place.PlaceMapper.toPlace(
			new Place.PlaceNameAndCode("Seoul Tower", areaCode),
			new Place.Position(37.5512, 126.9882),
			new Place.WeatherPosition(60, 127)
		);

		placeRepository.save(place);
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

		weatherCongestionDataProcessor.process();

		assertThat(weatherRepository.count()).isEqualTo(1);
		assertThat(congestionRepository.count()).isEqualTo(1);
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
