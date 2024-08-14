package hiyen.galmanhae.dataprocess.application;

import hiyen.galmanhae.dataprocess.client.CongestionClient;
import hiyen.galmanhae.dataprocess.client.response.CongestionResponse;
import hiyen.galmanhae.dataprocess.exception.DataProcessCheckedException.FailFetchAPIException;
import hiyen.galmanhae.dataprocess.exception.DataProcessUncheckedException.FailFetchAPIUncheckedException;
import hiyen.galmanhae.place.domain.vo.Congestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CongestionService {

	private final CongestionClient congestionClient;

	public Congestion fetch(final String areaCode) {

		final CongestionResponse response;
		try {
			response = congestionClient.fetch(areaCode);
		} catch (FailFetchAPIException e) {
			throw new FailFetchAPIUncheckedException(e);
		}

		return Congestion.of(Integer.valueOf(response.populationMin()), response.congestionLevel());
	}
}
