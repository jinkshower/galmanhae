package hiyen.galmanhae.data.domain;

public record WeatherAndCongestion(
	long placeId,
	Weather weather,
	Congestion congestion
) {

}
