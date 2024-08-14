package hiyen.galmanhae.dataprocess;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class DataProcessorTest {

	@Autowired
	private DataProcessor dataProcessor;

	@Test
	void testCallCongestionClient() {
		dataProcessor.process();
	}
}
