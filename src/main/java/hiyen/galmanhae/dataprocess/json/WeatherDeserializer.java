package hiyen.galmanhae.dataprocess.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import hiyen.galmanhae.dataprocess.dto.WeatherDTO;
import java.io.IOException;

public class WeatherDeserializer extends JsonDeserializer<WeatherDTO> {

	@Override
	public WeatherDTO deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext)
		throws IOException {
		ObjectCodec codec = jsonParser.getCodec();
		JsonNode rootNode = codec.readTree(jsonParser);

		JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

		String temperature = null;
		String rainingProbability = null;

		for (JsonNode itemNode : itemsNode) {
			String category = itemNode.get("category").asText();

			if ("TMP".equals(category)) {
				temperature = itemNode.get("fcstValue").asText();  // TMP는 기온 값
			} else if ("POP".equals(category)) {
				rainingProbability = itemNode.get("fcstValue").asText();  // POP는 강수확률
			}
		}

		return new WeatherDTO(temperature, rainingProbability);
	}
}
