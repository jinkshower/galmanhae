package hiyen.galmanhae.dataprocess.application;

import feign.Response;
import hiyen.galmanhae.dataprocess.client.PlaceClient;
import hiyen.galmanhae.dataprocess.exception.DataProcessUncheckedException.FailFetchAPIUncheckedException;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceFetchService {

	private final PlaceClient placeClient;

	public InputStream fetch() {
		Response response;
		try {
			response = placeClient.fetch();
		} catch (Exception e) {
			throw new FailFetchAPIUncheckedException(e);
		}

		try {
			return response.body().asInputStream();
		} catch (IOException e) {
			throw new FailFetchAPIUncheckedException(e);
		}
	}
}
