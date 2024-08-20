package hiyen.galmanhae.dataprocess.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import hiyen.galmanhae.dataprocess.exception.DataProcessCheckedException.FailFetchAPIException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(final String s, final Response response) {
		log.warn("response: {}", response.reason());
		return new FailFetchAPIException(response.reason());
	}
}
