package hiyen.galmanhae.dataprocess.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import hiyen.galmanhae.dataprocess.exception.DataProcessUncheckedException;
import hiyen.galmanhae.dataprocess.exception.DataProcessUncheckedException.InvalidDataException;
import java.util.List;
import java.util.Objects;

/**
 * 기상청 단기예보 API 응답 클래스 필요한 정보는
 * 기온(TMP) 강수확률(POP) 카테고리의 forecastValue
 */
public record WeatherResponse(
	@JsonProperty("response") Response response
) {

	public WeatherResponse {
		if (Objects.isNull(response)) {
			throw new InvalidDataException();
		}
	}

	public String getTemperature() {
		return findForecastValueByCategory("TMP");
	}

	public String getRainingProbability() {
		return findForecastValueByCategory("POP");
	}

	private String findForecastValueByCategory(String category) {
		for (final Item item : response.body().items().item()) {
			if (category.equals(item.category())) {
				return item.forecastValue();
			}
		}
		return null;
	}

	private record Response(
		@JsonProperty("header") Header header,
		@JsonProperty("body") Body body
	) {

	}

	private record Header(
		@JsonProperty("resultCode") String resultCode,
		@JsonProperty("resultMsg") String resultMessage
	) {

	}

	private record Body(
		@JsonProperty("dataType") String dataType,
		@JsonProperty("items") Items items,
		@JsonProperty("pageNo") int pageNo,
		@JsonProperty("numOfRows") int numOfRows,
		@JsonProperty("totalCount") int totalCount
	) {

	}

	private record Items(
		@JsonProperty("item") List<Item> item
	) {

	}

	private record Item(
		@JsonProperty("baseDate") String baseDate,
		@JsonProperty("baseTime") String baseTime,
		@JsonProperty("category") String category,
		@JsonProperty("fcstDate") String forecastDate,
		@JsonProperty("fcstTime") String forecastTime,
		@JsonProperty("fcstValue") String forecastValue,
		@JsonProperty("nx") int nx,
		@JsonProperty("ny") int ny
	) {

	}
}
