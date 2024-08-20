package hiyen.galmanhae.dataprocess.util;

import org.springframework.stereotype.Component;

/**
 * 람베르트 좌표계 변환기
 * 위도와 경도를 입력받아 람베르트 좌표계로 변환값을 반환한다.
 */
@Component
public class LambertCoordinateConverter {

	// Lambert Conformal Conic 투영법에 필요한 상수들
	private static final double PI = Math.asin(1.0) * 2.0;
	private static final double DEGRAD = PI / 180.0;
	private static final double Re = 6371.00877;  // 사용할 지구반경 [km]
	private static final double grid = 5.0;  // 격자간격 [km]
	private static final double slat1 = 30.0 * DEGRAD;  // 표준위도 1 [radian]
	private static final double slat2 = 60.0 * DEGRAD;  // 표준위도 2 [radian]
	private static final double olon = 126.0 * DEGRAD;  // 기준점 경도 [radian]
	private static final double olat = 38.0 * DEGRAD;  // 기준점 위도 [radian]
	private static final double xo = 43;  // 기준점의 X좌표 [격자거리]
	private static final double yo = 136;  // 기준점의 Y좌표 [격자거리]

	private final double sn; // 위도에 따른 비율
	private final double sf; // 비율에 따른 축적
	private final double ro; // 기준점에서의 거리

	// 생성자를 통해 초기화
	public LambertCoordinateConverter() {
		this.sn = calculateSn();
		this.sf = calculateSf();
		this.ro = calculateRo();
	}

	// 위도와 경도를 Lambert Conformal Conic 투영법을 통해 X, Y 좌표로 변환
	public int[] convert(final double latitude, double longitude) {
		double ra = Math.tan(PI * 0.25 + latitude * DEGRAD * 0.5);
		ra = Re / grid * sf / Math.pow(ra, sn);
		double theta = longitude * DEGRAD - olon;
		if (theta > PI) theta -= 2.0 * PI;
		if (theta < -PI) theta += 2.0 * PI;
		theta *= sn;

		int x = (int) (ra * Math.sin(theta) + xo + 1.5);
		int y = (int) (ro - ra * Math.cos(theta) + yo + 1.5);

		return new int[]{x, y};
	}

	private double calculateSn() {
		double sn = Math.tan(PI * 0.25 + slat2 * 0.5) / Math.tan(PI * 0.25 + slat1 * 0.5);
		return Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
	}

	private double calculateSf() {
		double sf = Math.tan(PI * 0.25 + slat1 * 0.5);
		return Math.pow(sf, sn) * Math.cos(slat1) / sn;
	}

	private double calculateRo() {
		double ro = Math.tan(PI * 0.25 + olat * 0.5);
		return Re / grid * sf / Math.pow(ro, sn);
	}
}
