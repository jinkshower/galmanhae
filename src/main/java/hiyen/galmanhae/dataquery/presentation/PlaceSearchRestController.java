package hiyen.galmanhae.dataquery.presentation;

import hiyen.galmanhae.dataquery.application.PlaceQueryService;
import hiyen.galmanhae.dataquery.response.PlaceSearchResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PlaceSearchRestController {

	private final PlaceQueryService placeQueryService;

	/**
	 * 검색의 결과를 포함하는 장소 이름(들)을 반환한다
	 */
	@GetMapping("/search/{keyword}")
	public ResponseEntity<List<PlaceSearchResponse>> search(@PathVariable final String keyword) {
		List<PlaceSearchResponse> response = placeQueryService.search(keyword);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/autocomplete/{keyword}")
	public ResponseEntity<List<PlaceSearchResponse>> autoComplete(@PathVariable final String keyword) {
		List<PlaceSearchResponse> response = placeQueryService.autoComplete(keyword);
		return ResponseEntity.ok(response);
	}
}
