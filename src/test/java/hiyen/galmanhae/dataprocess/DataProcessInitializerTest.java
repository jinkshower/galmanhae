package hiyen.galmanhae.dataprocess;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("prod")
class DataProcessInitializerTest {

	@MockBean
	private PlaceDataProcessor placeDataProcessor;

	@MockBean
	private WeatherCongestionDataProcessor weatherCongestionDataProcessor;

	@DisplayName("애플리케이션 시작시 필요한 데이터를 처리한다")
	@Test
	void testInit() {
		verify(weatherCongestionDataProcessor, times(1)).process();
		verify(placeDataProcessor, times(1)).process();
	}
}
