package hiyen.galmanhae.data.domain;

public record Weather(

	double temperature,
	double rainingProbability
) {

	public static Weather of(final double temperature, final double rainingProbability) {
		return new Weather(temperature, rainingProbability);
	}
}
