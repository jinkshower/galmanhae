package hiyen.galmanhae.dataprocess;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("prod")
@RequiredArgsConstructor
@Component
public class DataProcessInitializer {

	private final PlaceInfoDataProcessor placeInfoDataProcessor;
	private final DataProcessor dataProcessor;

	/**
	 * 애플리케이션 시작시 장소 목록을 다운로드 이후 DB에 저장 해당 장소 목록을 읽어 외부 API를 호출하여 데이터를 가져오고 Place 테이블에 저장
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		log.info("애플리케이션이 시작되고 데이터 처리를 시작합니다");
		placeInfoDataProcessor.process();
		dataProcessor.process();
		log.info("데이터 처리가 완료되었습니다.");
	}

	/**
	 * 매 시간마다 외부 API를 호출하여 데이터를 가져오고 Place 테이블에 저장
	 */
	@Scheduled(cron = "0 0 * * * ?")
	public void hourlyProcess() {
		log.info("매 시간마다 최신의 인구, 날씨 데이터를 처리합니다. 현재 시간 {}", LocalDateTime.now());
		dataProcessor.process();
		log.info("매 시간마다 최신의 인구, 날씨 데이터 처리가 완료되었습니다. 현재 시간 {}", LocalDateTime.now());
	}
}
