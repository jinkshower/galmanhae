package hiyen.galmanhae.dataprocess.application;

import static org.assertj.core.api.Assertions.*;

import hiyen.galmanhae.dataprocess.csv.LambertCoordinateConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LambertCoordinateConverterTest {

	private final LambertCoordinateConverter lambertCoordinateConverter = new LambertCoordinateConverter();

	@DisplayName("위도와 경도를 람베르트 X,Y로 변환할 수 있다")
	@Test
	void success() {
		final double latitude = 37.5112843816696;
		final double longitude = 127.06005155384705;

		int[] convert = lambertCoordinateConverter.convert(latitude, longitude);

		assertThat(convert).isNotNull();
		assertThat(convert).hasSize(2);
		assertThat(convert[0]).isEqualTo(62);
		assertThat(convert[1]).isEqualTo(127);
	}
}
