package hiyen.galmanhae.dataprocess.csv;

import hiyen.galmanhae.dataprocess.csv.PlaceInfo.AreaInfo;
import hiyen.galmanhae.dataprocess.csv.PlaceInfo.LocationInfo;
import hiyen.galmanhae.dataprocess.csv.PlaceInfo.WeatherInfo;
import hiyen.galmanhae.dataprocess.exception.DataProcessingException.FailReadingFileException;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DataParser {

	private static final String DELIMITER = ",";
	private final String filePath;

	public DataParser(@Value("${dataprocess.locationfile.path}") final String filePath) {
		this.filePath = filePath;
	}

	/*
	 * CSV 파일을 읽고 PlaceInfo 객체로 변환한다
	 */
	public List<PlaceInfo> readCSV() {
		final List<PlaceInfo> placeInfos;

		try (final BufferedReader reader = Files.newBufferedReader(
			Paths.get(getClass().getClassLoader().getResource(filePath).toURI()))) {
			placeInfos = reader.lines()
				.skip(1) // skip header
				.map(line -> line.split(DELIMITER)) // split by delimiter
				.map(tokens -> new PlaceInfo(
					new AreaInfo(tokens[1], tokens[2]),
					new LocationInfo(tokens[3], tokens[4]),
					new WeatherInfo(tokens[5], tokens[6])
				))// map to PlaceInfo
				.toList();
		} catch (IOException | URISyntaxException | NullPointerException e) {
			throw new FailReadingFileException(e);
		}

		return placeInfos;
	}
}
