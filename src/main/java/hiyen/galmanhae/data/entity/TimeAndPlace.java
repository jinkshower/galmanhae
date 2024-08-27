package hiyen.galmanhae.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class TimeAndPlace implements Serializable {

	@Column
	private LocalDateTime measuredAt;
	@Column
	private Long placeId;
}
