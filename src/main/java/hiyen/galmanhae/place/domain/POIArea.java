package hiyen.galmanhae.place.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class POIArea {

	@Id
	@Column(name = "area_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String code;
	private String latitude;
	private String longitude;
	private String weatherX;
	private String weatherY;

	@Builder
	public POIArea(String name, String code, String latitude, String longitude, String weatherX, String weatherY) {
		this(null, name, code, latitude, longitude, weatherX, weatherY);
	}
}
