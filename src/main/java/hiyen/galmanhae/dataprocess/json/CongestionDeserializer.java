package hiyen.galmanhae.dataprocess.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import hiyen.galmanhae.dataprocess.dto.CongestionResponse;
import java.io.IOException;

public class CongestionDeserializer extends JsonDeserializer<CongestionResponse> {

	@Override
	public CongestionResponse deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext)
		throws IOException {
		final ObjectCodec codec = jsonParser.getCodec();
		final JsonNode rootNode = codec.readTree(jsonParser);

		// "SeoulRtd.citydata_ppltn"에서 첫 번째 객체를 가져옵니다.
		final JsonNode itemNode = rootNode.path("SeoulRtd.citydata_ppltn").get(0);

		final String congestionLevel = itemNode.get("AREA_CONGEST_LVL").asText();
		final String populationMin = itemNode.get("AREA_PPLTN_MIN").asText();

		return new CongestionResponse(congestionLevel, populationMin);
	}
}
