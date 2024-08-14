package hiyen.galmanhae.dataprocess.application;

import hiyen.galmanhae.dataprocess.client.CongestionClient;
import hiyen.galmanhae.dataprocess.client.response.CongestionResponse;
import hiyen.galmanhae.place.domain.vo.Congestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CongestionService {

	private final CongestionClient congestionClient;

	public Congestion fetch(final String areaCode) {

		final CongestionResponse response = congestionClient.fetch(areaCode);

		return Congestion.of(Integer.valueOf(response.populationMin()), response.congestionLevel());
	}
}
