package hiyen.galmanhae.dataprocess.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import hiyen.galmanhae.dataprocess.json.CongestionDeserializer;

@JsonDeserialize(using = CongestionDeserializer.class)
public record CongestionResponse(

	String congestionLevel,
	String populationMin
) {

}
