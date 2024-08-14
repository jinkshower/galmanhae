package hiyen.galmanhae.dataprocess;

import static org.assertj.core.api.Assertions.*;

import hiyen.galmanhae.dataprocess.csv.DataParser;
import hiyen.galmanhae.dataprocess.csv.PlaceInfo;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DataParserTest {

	private static final String TEST_CSV_FILE = "test-location_mapping.csv";

	private final DataParser dataParser = new DataParser(TEST_CSV_FILE);

	@DisplayName("CSV 파일을 읽어 PlaceInfo 객체로 변환한다")
	@Test
	void testReadCSV() {
		List<PlaceInfo> result = dataParser.readCSV();

		assertThat(result).isNotEmpty();
		assertThat(result).hasSize(1);
	}
}
