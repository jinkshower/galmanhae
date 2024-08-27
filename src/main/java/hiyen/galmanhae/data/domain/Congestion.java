package hiyen.galmanhae.data.domain;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public record Congestion(

	int currentPeople,
	String congestionIndicator

) {

	public static Congestion of(final int currentPeople, final String congestionIndicator) {
		return new Congestion(currentPeople, congestionIndicator);
	}
}