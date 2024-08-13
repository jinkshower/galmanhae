package hiyen.galmanhae.dataprocess.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import hiyen.galmanhae.dataprocess.dto.CongestionDTO;
import java.io.IOException;

public class CongestionDeserializer extends JsonDeserializer<CongestionDTO> {

	@Override
	public CongestionDTO deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext)
		throws IOException {
		ObjectCodec codec = jsonParser.getCodec();
		JsonNode rootNode = codec.readTree(jsonParser);

		// "SeoulRtd.citydata_ppltn"에서 첫 번째 객체를 가져옵니다.
		JsonNode itemNode = rootNode.path("SeoulRtd.citydata_ppltn").get(0);

		String areaName = itemNode.get("AREA_NM").asText();
		String congestionLevel = itemNode.get("AREA_CONGEST_LVL").asText();
		String populationMin = itemNode.get("AREA_PPLTN_MIN").asText();

		return new CongestionDTO(areaName, congestionLevel, populationMin);
	}
}
