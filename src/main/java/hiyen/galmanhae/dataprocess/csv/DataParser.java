package hiyen.galmanhae.dataprocess.csv;

import hiyen.galmanhae.dataprocess.csv.PlaceInfo.AreaInfo;
import hiyen.galmanhae.dataprocess.csv.PlaceInfo.LocationInfo;
import hiyen.galmanhae.dataprocess.csv.PlaceInfo.WeatherInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DataParser {

	private static final String CSV_FILE = "src/main/resources/location_mapping.csv";
	private static final String DELIMITER = ",";

	/*
	 * CSV 파일을 읽고 PlaceInfo 객체로 변환한다
	 */
	public List<PlaceInfo> readCSV() {
		List<PlaceInfo> placeInfos;

		try (final BufferedReader reader = Files.newBufferedReader(Paths.get(CSV_FILE))) {
			placeInfos = reader.lines()
				.skip(1) // skip header
				.map(line -> line.split(DELIMITER)) // split by delimiter
				.map(tokens -> new PlaceInfo(
					new AreaInfo(tokens[1], tokens[2]),
					new LocationInfo(tokens[3], tokens[4]),
					new WeatherInfo(tokens[5], tokens[6])
				))// map to PlaceInfo
				.toList();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return placeInfos;
	}
}
