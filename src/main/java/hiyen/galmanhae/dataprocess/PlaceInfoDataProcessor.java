package hiyen.galmanhae.dataprocess;

import hiyen.galmanhae.dataprocess.application.DataQueryService;
import hiyen.galmanhae.dataprocess.application.PlaceInfoService;
import hiyen.galmanhae.dataprocess.util.DataParser;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo;
import hiyen.galmanhae.dataprocess.exception.DataProcessUncheckedException.FailReadingFileException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlaceInfoDataProcessor {

	private final PlaceInfoService placeInfoService;
	private final DataParser dataParser;
	private final DataQueryService dataQueryService;

	@Value("${client.placeinfo.filename}")
	private String fileName;

	/**
	 * 장소 정보를 다운로드하여 db에 저장
	 */
	//TODO 애플리케이션 시작시 해당 메서드 실행하는 걸 테스트할 방법 필요
//	@EventListener(ApplicationReadyEvent.class)
	public void process() {
		log.info("장소 정보 다운로드 및 저장 시작");
		final InputStream fetch = placeInfoService.fetch();
		List<PlaceInfo> placeInfos;
		try {
			Map<String, byte[]> fileMap = dataParser.processZipFile(fetch);
			placeInfos = dataParser.parse(fileMap, fileName);
		} catch (Exception e) {
			throw new FailReadingFileException(e);
		}
		dataQueryService.saveAllPlaceInfos(placeInfos);
	}
}
