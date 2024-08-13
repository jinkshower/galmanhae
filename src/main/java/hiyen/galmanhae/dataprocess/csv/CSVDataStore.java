package hiyen.galmanhae.dataprocess.csv;

import hiyen.galmanhae.dataprocess.csv.PlaceInfo.AreaInfo;
import hiyen.galmanhae.dataprocess.csv.PlaceInfo.LocationInfo;
import hiyen.galmanhae.dataprocess.csv.PlaceInfo.WeatherInfo;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CSVDataStore {

	private final DataParser dataParser;
	private List<PlaceInfo> placeInfos;

	@PostConstruct
	public void initializeData() {
		placeInfos = dataParser.readCSV();
	}

	public List<PlaceInfo> getPlaceInfos() {
		return placeInfos;
	}
}
