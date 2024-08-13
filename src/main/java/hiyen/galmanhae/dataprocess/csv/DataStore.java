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
public class DataStore {

	private final DataParser dataParser;
	private List<PlaceInfo> placeInfos;

	@PostConstruct
	public void initializeData() {
		placeInfos = dataParser.readCSV();
	}

	public List<PlaceInfo> getPlaceInfos() {
		return placeInfos;
	}

	public List<AreaInfo> getAreas() {
		return placeInfos.stream()
			.map(PlaceInfo::areaInfo)
			.toList();
	}

	public List<LocationInfo> getLocations() {
		return placeInfos.stream()
			.map(PlaceInfo::locationInfo)
			.toList();
	}

	public List<WeatherInfo> getWeathers() {
		return placeInfos.stream()
			.map(PlaceInfo::weatherInfo)
			.toList();
	}
}
