package hiyen.galmanhae.dataprocess.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import hiyen.galmanhae.dataprocess.json.WeatherDeserializer;

@JsonDeserialize(using = WeatherDeserializer.class)
public record WeatherDTO(

	String temperature,
	String rainingProbability
) {

}
