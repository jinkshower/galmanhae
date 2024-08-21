package hiyen.galmanhae.dataprocess;

import hiyen.galmanhae.place.domain.place.Place;
import hiyen.galmanhae.place.repository.PlaceInfoRepository;
import hiyen.galmanhae.place.repository.PlaceRepository;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/*
 * 전 과정을 테스트하기 위한 개발에 사용하는 클래스.
 * @Disabled를 활용해 개발환경에서 사용중.
 */
@SpringBootTest
@Disabled
public class DataProcessTest {

	@Autowired
	private PlaceInfoDataProcessor placeInfoDataProcessor;

	@Autowired
	private DataProcessor dataProcessor;

	@Autowired
	private PlaceInfoRepository placeInfoRepository;

	@Autowired
	private PlaceRepository placeRepository;

	@Test
	void test() {
		placeInfoDataProcessor.process();
		dataProcessor.process();

		List<Place> all = placeRepository.findAll();
		all.forEach(System.out::println);
		System.out.println(all.size());
	}
}
