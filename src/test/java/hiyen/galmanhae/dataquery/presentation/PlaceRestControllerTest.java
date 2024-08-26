package hiyen.galmanhae.dataquery.presentation;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import hiyen.galmanhae.common.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

class PlaceRestControllerTest extends AcceptanceTest {

	@Nested
	@DisplayName("장소 단일 조회")
	class PlaceInfo {

		final String validPlaceName = "강남 MICE 관광특구";
		final String malformedPlaceName = "우리 집";

		@Test
		@DisplayName("성공")
		@Sql("/test-initialize.sql")
		void success() {
			final ExtractableResponse<Response> result = getPlace(validPlaceName);

			assertAll(
				() -> assertThat(result.statusCode()).isEqualTo(200),
				() -> assertThat(result.body().jsonPath().getString("name")).isEqualTo("강남 MICE 관광특구"),
				() -> assertThat(result.body().jsonPath().getString("goOutLevelDescription")).isEqualTo("외출하기 좋아요!")
			);
		}

		@Test
		@DisplayName("없는 장소 입력시 실패한다")
		void fail_placeNotFound() {
			final ExtractableResponse<Response> result = getPlace(malformedPlaceName);

			assertThat(result.statusCode()).isEqualTo(404);
		}
	}

	private ExtractableResponse<Response> getPlace(final String placeName) {
		return RestAssured.given().log().all()
			.pathParam("placeName", placeName)
			.when().get("/api/places/{placeName}")
			.then().log().all()
			.extract();
	}
}
