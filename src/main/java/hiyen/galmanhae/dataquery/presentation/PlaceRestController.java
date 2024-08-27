package hiyen.galmanhae.dataquery.presentation;

import hiyen.galmanhae.dataquery.application.PlaceQueryService;
import hiyen.galmanhae.dataquery.response.PlaceDetailResponse;
import hiyen.galmanhae.dataquery.response.PlaceResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceRestController {

	private final PlaceQueryService placeQueryService;

	/**
	 * 지도에 표시할 장소 정보를 모두 조회한다 모든 장소의 이름, 위도, 경도, 외출등급을 반환한다
	 */
	@GetMapping
	public ResponseEntity<List<PlaceResponse>> getAllPlaces() {
		final List<PlaceResponse> response = placeQueryService.getAllPlaces();
		return ResponseEntity.ok(response);
	}

	/**
	 * 특정 장소의 장소 정보(외출도, 날씨 문장, 날씨 상세(온도, 강수확률), 혼잡도 문장, 혼잡도 상세(실시간 인구수)를 반환한다
	 */
	@GetMapping("/{placeId}")
	public ResponseEntity<PlaceDetailResponse> getPlace(@PathVariable Long placeId) {
		final PlaceDetailResponse response = placeQueryService.getPlace(placeId);
		return ResponseEntity.ok(response);
	}
}
