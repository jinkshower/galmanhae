package hiyen.galmanhae.dataquery.presentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlaceWebController {

	@Value("${kakao.map.app-key}")
	private String appKey;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("appKey", appKey);
		return "map";
	}
}
