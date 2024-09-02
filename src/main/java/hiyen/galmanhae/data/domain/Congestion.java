package hiyen.galmanhae.data.domain;

public record Congestion(
	Long placeId,
	int currentPeople,
	String congestionIndicator

) {

	public static Congestion of(final int currentPeople, final String congestionIndicator) {
		return new Congestion(null, currentPeople, congestionIndicator);
	}
}
