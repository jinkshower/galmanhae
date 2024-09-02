package hiyen.galmanhae.dataprocess;

import hiyen.galmanhae.data.domain.Place;
import hiyen.galmanhae.data.repository.PlaceRepository;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/*
 * 전 과정을 테스트하기 위한 개발에 사용하는 클래스.
 * @Disabled를 활용해 개발환경에서 사용중.
 */
@SpringBootTest
@ActiveProfiles("local")
@Disabled
public class DataProcessTest {

	@Autowired
	private PlaceDataProcessor placeDataProcessor;

	@Autowired
	private WeatherCongestionDataProcessor weatherCongestionDataProcessor;

	@Autowired
	private PlaceRepository placeRepository;

	@Test
	@Transactional
	void test() {
		placeDataProcessor.process();
		weatherCongestionDataProcessor.process();

		List<Place> all = placeRepository.findAll();
		all.forEach(System.out::println);
		System.out.println(all.size());
	}
}
