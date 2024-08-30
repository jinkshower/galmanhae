package hiyen.galmanhae.data.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CongestionTest {

	@Test
	void create() {
		final Congestion congestion = Congestion.of(10, "매우 혼잡");

		assertThat(congestion).isNotNull();
		assertThat(congestion.currentPeople()).isEqualTo(10);
		assertThat(congestion.congestionIndicator()).isEqualTo("매우 혼잡");
	}
}
