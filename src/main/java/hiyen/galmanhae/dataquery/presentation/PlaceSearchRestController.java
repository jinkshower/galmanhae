package hiyen.galmanhae.dataquery.presentation;

import hiyen.galmanhae.dataquery.application.PlaceInfoQueryService;
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
@RequestMapping("/api/search")
public class PlaceSearchRestController {

	private final PlaceInfoQueryService placeInfoQueryService;

	@GetMapping("/{keyword}")
	public ResponseEntity<List<PlaceSearchResponse>> search(@PathVariable final String keyword) {
		List<PlaceSearchResponse> response = placeInfoQueryService.search(keyword);
		return ResponseEntity.ok(response);
	}
}
