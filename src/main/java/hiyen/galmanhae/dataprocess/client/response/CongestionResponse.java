package hiyen.galmanhae.dataprocess.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * 서울시 실시간 인구데이터 API 응답 클래스 필요한 정보는
 * 지역 혼잡도 지표(AREA_CONGEST_LVL)와 실시간 인구수(AREA_PPLTN_MIN)
 */
public record CongestionResponse(
	@JsonProperty("SeoulRtd.citydata_ppltn") List<CityData> seoulCityDatas,
	@JsonProperty("RESULT") Result result
) {

	public String getCongestionLevel() {
		return seoulCityDatas.get(0).areaCongestionLevel();
	}

	public String getPopulation() {
		return seoulCityDatas.get(0).areaPopulationMin();
	}

	private record CityData(
		@JsonProperty("AREA_NM") String areaName,
		@JsonProperty("AREA_CD") String areaCode,
		@JsonProperty("AREA_CONGEST_LVL") String areaCongestionLevel,
		@JsonProperty("AREA_CONGEST_MSG") String areaCongestionMessage,
		@JsonProperty("AREA_PPLTN_MIN") String areaPopulationMin,
		@JsonProperty("AREA_PPLTN_MAX") String areaPopulationMax,
		@JsonProperty("MALE_PPLTN_RATE") String malePopulationRate,
		@JsonProperty("FEMALE_PPLTN_RATE") String femalePopulationRate,
		@JsonProperty("PPLTN_RATE_0") String populationRate0,
		@JsonProperty("PPLTN_RATE_10") String populationRate10,
		@JsonProperty("PPLTN_RATE_20") String populationRate20,
		@JsonProperty("PPLTN_RATE_30") String populationRate30,
		@JsonProperty("PPLTN_RATE_40") String populationRate40,
		@JsonProperty("PPLTN_RATE_50") String populationRate50,
		@JsonProperty("PPLTN_RATE_60") String populationRate60,
		@JsonProperty("PPLTN_RATE_70") String populationRate70,
		@JsonProperty("RESNT_PPLTN_RATE") String residentPopulationRate,
		@JsonProperty("NON_RESNT_PPLTN_RATE") String nonResidentPopulationRate,
		@JsonProperty("REPLACE_YN") String replaceYesNo,
		@JsonProperty("PPLTN_TIME") String populationTime,
		@JsonProperty("FCST_YN") String forecastYesNo,
		@JsonProperty("FCST_PPLTN") List<ForecastPopulation> forecastPopulation
	) {

	}

	private record ForecastPopulation(
		@JsonProperty("FCST_TIME") String forecastTime,
		@JsonProperty("FCST_CONGEST_LVL") String forecastCongestionLevel,
		@JsonProperty("FCST_PPLTN_MIN") String forecastPopulationMin,
		@JsonProperty("FCST_PPLTN_MAX") String forecastPopulationMax
	) {

	}

	private record Result(
		@JsonProperty("RESULT.CODE") String resultCode,
		@JsonProperty("RESULT.MESSAGE") String resultMessage
	) {

	}
}
