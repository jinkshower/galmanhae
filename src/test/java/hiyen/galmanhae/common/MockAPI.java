package hiyen.galmanhae.common;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
	"client.weather.url=http://localhost:${wiremock.server.port}",
	"client.congestion.url=http://localhost:${wiremock.server.port}",
	"client.placeinfo.url=http://localhost:${wiremock.server.port}"
})
public abstract class MockAPI {

	@Autowired
	private WireMockServer wireMockServer;

	@BeforeEach
	void setUp() {
		wireMockServer.stop();
		wireMockServer.start();
	}

	@AfterEach
	void reset() {
		wireMockServer.resetAll();
	}

}
