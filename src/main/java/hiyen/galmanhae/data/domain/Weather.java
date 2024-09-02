package hiyen.galmanhae.data.domain;

public record Weather(

	Long placeId,
	double temperature,
	double rainingProbability
) {

	public static Weather of(final double temperature, final double rainingProbability) {
		return new Weather(null, temperature, rainingProbability);
	}
}
