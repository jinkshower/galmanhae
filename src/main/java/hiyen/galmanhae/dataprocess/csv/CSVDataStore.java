package hiyen.galmanhae.dataprocess.csv;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CSVDataStore {

	private final DataParser dataParser;
	private List<PlaceInfo> placeInfos = new ArrayList<>();

	@PostConstruct
	public void initializeData() {
		placeInfos = new ArrayList<>(dataParser.readCSV());
	}

	public List<PlaceInfo> getPlaceInfos() {
		return new ArrayList<>(placeInfos);
	}
}
