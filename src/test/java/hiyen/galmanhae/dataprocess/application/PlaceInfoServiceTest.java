package hiyen.galmanhae.dataprocess.application;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import hiyen.galmanhae.common.MockAPI;
import hiyen.galmanhae.dataprocess.exception.DataProcessUncheckedException.FailFetchAPIUncheckedException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PlaceInfoServiceTest extends MockAPI {

	@Autowired
	private PlaceInfoService placeInfoService;

	@DisplayName("Place Info API 호출")
	@Nested
	class PlacInfoAPIFetch {

		@DisplayName("성공")
		@Test
		void success() throws URISyntaxException, IOException {
			setupStub(aResponse()
				.withStatus(200)
				.withHeader("Content-Type", "application/x-msdownload")
				.withBody(validPlaceInfoResponse())
			);

			final InputStream actual = placeInfoService.fetch();

			assertThat(actual).isNotNull();
			assertThat(actual.available()).isGreaterThan(0);
		}

		@DisplayName("실패 - readtimeout")
		@Test
		void fail_readTimeOut() {

			setupStub(aResponse()
				.withFixedDelay(5000)
			);

			assertThatThrownBy(() -> placeInfoService.fetch())
				.isInstanceOf(FailFetchAPIUncheckedException.class);
		}
	}

	private void setupStub(final ResponseDefinitionBuilder responseBuilder) {
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
