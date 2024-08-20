package hiyen.galmanhae.dataprocess.application;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import hiyen.galmanhae.dataprocess.MockAPI;
import hiyen.galmanhae.dataprocess.exception.DataProcessUncheckedException.FailFetchAPIUncheckedException;
import hiyen.galmanhae.place.domain.place.Congestion;
import hiyen.galmanhae.place.domain.place.Congestion.CongestionLevel;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

class CongestionServiceTest extends MockAPI {

	@Autowired
	private CongestionService congestionService;

	@Value("${client.congestion.service-key}")
	private String serviceKey;

	@DisplayName("혼잡도 API 호출")
	@Nested
	class CongestionAPIFetch {

		final String areaCode = "POI1001";
		final Integer expectedCongestion = 500;

		@DisplayName("성공")
		@Test
		void success() throws URISyntaxException, IOException {
			final String validResponse = validResponse();

			setupStub(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "application/json")
				.withBody(validResponse), areaCode
			);

			final Congestion actual = congestionService.fetch(areaCode);

			assertThat(actual).isNotNull();
			assertThat(actual.congestionLevel()).isEqualTo(CongestionLevel.NORMAL);
			assertThat(actual.congestionPeople()).isEqualTo(expectedCongestion);
		}

		@DisplayName("실패 - readtimeout")
		@Test
		void fail_readTimeOut() {
			setupStub(aResponse()
				.withFixedDelay(5000), areaCode
			);

			assertThatThrownBy(() -> congestionService.fetch(areaCode))
				.isInstanceOf(FailFetchAPIUncheckedException.class);
		}

		@DisplayName("실패 - 200OK 예외")
		@Test
		void fail_200OK() {
			setupStub(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "application/json")
				.withBody("fail"), areaCode
			);

			assertThatThrownBy(() -> congestionService.fetch(areaCode))
				.isInstanceOf(FailFetchAPIUncheckedException.class);
		}

		@DisplayName("실패 - 200이 아닌 응답")
		@Test
		void fail_not200() {
			setupStub(aResponse()
				.withStatus(500), areaCode
			);

			assertThatThrownBy(() -> congestionService.fetch(areaCode))
				.isInstanceOf(FailFetchAPIUncheckedException.class);
		}
	}

	private void setupStub(final ResponseDefinitionBuilder responseBuilder, final String areaCode) {
		stubFor(get(urlPathEqualTo("/" + serviceKey + "/json/citydata_ppltn/1/1/" + areaCode))
			.willReturn(responseBuilder)
		);
	}

	private String validResponse() throws URISyntaxException, IOException {
		return new String(Files.readAllBytes(Paths.get(
			getClass().getClassLoader().getResource("valid-congestion-response.json").toURI()
		)));
	}
}
