package hiyen.galmanhae.dataprocess;

import hiyen.galmanhae.data.domain.Place;
import hiyen.galmanhae.dataprocess.application.DataQueryService;
import hiyen.galmanhae.dataprocess.application.PlaceFetchService;
import hiyen.galmanhae.dataprocess.exception.DataProcessUncheckedException.FailReadingFileException;
import hiyen.galmanhae.dataprocess.util.DataParser;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlaceDataProcessor {

	private final PlaceFetchService placeFetchService;
	private final DataParser dataParser;
	private final DataQueryService dataQueryService;

	@Value("${client.placeinfo.filename}")
	private String fileName;

	/**
	 * 장소 정보를 다운로드하여 db에 저장
	 */
	public void process() {
		log.info("장소 정보 다운로드 및 저장 시작");
		final InputStream fetch = placeFetchService.fetch();
		List<Place> places;
		try {
			Map<String, byte[]> fileMap = dataParser.processZipFile(fetch);
			places = dataParser.parse(fileMap, fileName);
		} catch (Exception e) {
			log.info("장소 정보 다운로드 및 저장 실패. 현재 시간 : {}", LocalDateTime.now(), e);
			throw new FailReadingFileException(e);
		}
		dataQueryService.saveAllPlaces(places);
	}
}
