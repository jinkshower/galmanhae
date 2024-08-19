package hiyen.galmanhae.dataprocess;

import hiyen.galmanhae.dataprocess.application.DataQueryService;
import hiyen.galmanhae.dataprocess.application.PlaceInfoService;
import hiyen.galmanhae.dataprocess.csv.DataParser;
import hiyen.galmanhae.dataprocess.csv.PlaceInfo;
import hiyen.galmanhae.dataprocess.exception.DataProcessUncheckedException.FailReadingFileException;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlaceInfoDataProcessor {

	private final PlaceInfoService placeInfoService;
	private final DataParser dataParser;
	private final DataQueryService dataQueryService;

	/**
	 * 장소 정보를 다운로드하여 db에 저장
	 */
	@PostConstruct
	public void process() {
		Map<String, byte[]> fetch = placeInfoService.fetch();
		List<PlaceInfo> placeInfos;
		try {
			placeInfos = dataParser.parse(fetch);
		} catch (Exception e) {
			throw new FailReadingFileException(e);
		}
		dataQueryService.saveAllPlaceInfos(placeInfos);
	}
}
