package hiyen.galmanhae.dataquery.presentation;

import hiyen.galmanhae.dataquery.application.PlaceQueryService;
import hiyen.galmanhae.dataquery.response.PlaceResponse;
import hiyen.galmanhae.place.domain.place.Place;
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

	@GetMapping("/{placeName}")
	public ResponseEntity<PlaceResponse> getPlace(@PathVariable String placeName) {
		final Place place = placeQueryService.getPlace(placeName);
		final PlaceResponse response = PlaceResponse.from(place);
		return ResponseEntity.ok(response);
	}
}
