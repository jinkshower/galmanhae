package hiyen.galmanhae.dataprocess.application;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import hiyen.galmanhae.common.MockAPI;
import hiyen.galmanhae.data.domain.Weather;
import hiyen.galmanhae.dataprocess.exception.DataProcessUncheckedException.FailFetchAPIUncheckedException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class WeatherFetchServiceTest extends MockAPI {

	@Autowired
	private WeatherFetchService weatherFetchService;

	@Nested
	@DisplayName("날씨 API 호출")
	class WeatherAPIFetch {

		final int weatherX = 52;
		final int weatherY = 127;
		final double expectedTemp = 20.0;
		final double expectedRaining = 30.0;

		@DisplayName("성공")
		@Test
		void success() throws URISyntaxException, IOException {
			final String validResponse = validResponse();

			setupStub(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "application/json")
				.withBody(validResponse)
			);

			final Weather actual = weatherFetchService.fetch(weatherX, weatherY);

			assertThat(actual).isNotNull();
			assertThat(actual.temperature()).isEqualTo(expectedTemp);
			assertThat(actual.rainingProbability()).isEqualTo(expectedRaining);
		}

		@DisplayName("실패 - readtimeout")
		@Test
		void fail_readTimeOut() {

			setupStub(aResponse()
				.withFixedDelay(5000)
			);

			assertThatThrownBy(() -> weatherFetchService.fetch(weatherX, weatherY))
				.isInstanceOf(FailFetchAPIUncheckedException.class);
		}


		@DisplayName("실패 - 200OK 예외")
		@Test
		void fail_200OK() {
			setupStub(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "application/json")
				.withBody("fail")
			);

			assertThatThrownBy(() -> weatherFetchService.fetch(weatherX, weatherY))
				.isInstanceOf(FailFetchAPIUncheckedException.class);
		}

		@DisplayName("실패 - 200이 아닌 응답")
		@Test
		void fail_not200() {
			setupStub(aResponse()
				.withStatus(500)
			);

			assertThatThrownBy(() -> weatherFetchService.fetch(weatherX, weatherY))
				.isInstanceOf(FailFetchAPIUncheckedException.class);
		}
	}

	private void setupStub(final ResponseDefinitionBuilder responseBuilder) {
		stubFor(get(urlPathEqualTo("/getVilageFcst"))
			.withQueryParam("serviceKey", matching(".*"))
			.withQueryParam("base_date", matching(".*"))
			.withQueryParam("base_time", matching(".*"))
			.withQueryParam("nx", matching(".*"))
			.withQueryParam("ny", matching(".*"))
			.willReturn(responseBuilder)
		);
	}

	private String validResponse() throws URISyntaxException, IOException {
		return new String(Files.readAllBytes(Paths.get(
			getClass().getClassLoader().getResource("valid-weather-response.json").toURI()
		)));
	}
}
