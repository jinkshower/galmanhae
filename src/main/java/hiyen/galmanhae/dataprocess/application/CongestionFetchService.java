package hiyen.galmanhae.dataprocess.application;

import hiyen.galmanhae.data.domain.Congestion;
import hiyen.galmanhae.dataprocess.client.CongestionClient;
import hiyen.galmanhae.dataprocess.client.response.CongestionResponse;
import hiyen.galmanhae.dataprocess.exception.DataProcessUncheckedException.FailFetchAPIUncheckedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CongestionFetchService {

	private final CongestionClient congestionClient;

	public Congestion fetch(final String areaCode) {

		final CongestionResponse response;
		try {
			response = congestionClient.fetch(areaCode);
		} catch (Exception e) {
			throw new FailFetchAPIUncheckedException(e);
		}

		return Congestion.of(Integer.parseInt(response.getPopulation()), response.getCongestionLevel());
	}
}
