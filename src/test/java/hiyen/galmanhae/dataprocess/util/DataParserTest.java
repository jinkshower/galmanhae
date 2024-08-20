package hiyen.galmanhae.dataprocess.util;

import static org.assertj.core.api.Assertions.*;

import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class DataParserTest {

	private final LambertCoordinateConverter lambertCoordinateConverter = new LambertCoordinateConverter();
	private final DataParser dataParser = new DataParser(lambertCoordinateConverter);

	@DisplayName("zip 파일을 읽어서 Map으로 변환한다")
	@Test
	void readZipFileConvertMap() throws IOException {
		final ClassPathResource resource = new ClassPathResource("test_location.zip");

		try (InputStream inputStream = resource.getInputStream()) {
			Map<String, byte[]> fileMap = dataParser.processZipFile(inputStream);
			assertThat(fileMap).isNotEmpty();
			assertThat(fileMap).containsKeys("test_location.dbf", "test_location.shp", "test_location.shx");
		}
	}

	@DisplayName("Map을 PlaceInfo로 변환한다")
	@Test
	void convertMapToPlaceInfo() throws IOException {
		final ClassPathResource resource = new ClassPathResource("test_location.zip");

		try (InputStream inputStream = resource.getInputStream()) {
			Map<String, byte[]> fileMap = dataParser.processZipFile(inputStream);
			List<PlaceInfo> actual = dataParser.parse(fileMap, "test_location");

			assertThat(actual).isNotEmpty();
			assertThat(actual.get(0).locationInfo().latitude()).isEqualTo("37.5112843816696");
			assertThat(actual.get(0).locationInfo().longitude()).isEqualTo("127.06005155384705");
			assertThat(actual.get(0).weatherInfo().weatherX()).isEqualTo("62");
			assertThat(actual.get(0).weatherInfo().weatherY()).isEqualTo("127");
		}
	}
}
