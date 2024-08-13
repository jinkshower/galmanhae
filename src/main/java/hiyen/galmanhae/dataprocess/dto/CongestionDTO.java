package hiyen.galmanhae.dataprocess.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import hiyen.galmanhae.dataprocess.json.CongestionDeserializer;

@JsonDeserialize(using = CongestionDeserializer.class)
public record CongestionDTO(

	String areaName,
	String congestionLevel,
	String populationMin
) {

}
