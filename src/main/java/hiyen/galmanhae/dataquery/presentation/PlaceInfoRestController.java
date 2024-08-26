package hiyen.galmanhae.dataquery.presentation;

import hiyen.galmanhae.dataquery.application.PlaceInfoQueryService;
import hiyen.galmanhae.dataquery.response.PlaceInfoResponse;
import hiyen.galmanhae.place.domain.placeinfo.PlaceInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/place-infos")
@RequiredArgsConstructor
public class PlaceInfoRestController {

	private final PlaceInfoQueryService placeInfoQueryService;

	@GetMapping
	public ResponseEntity<List<PlaceInfoResponse>> getPlaceInfos() {
		final List<PlaceInfo> placeInfos = placeInfoQueryService.getPlaceInfos();
		final List<PlaceInfoResponse> response = PlaceInfoResponse.listOf(placeInfos);
		return ResponseEntity.ok(response);
	}
}
