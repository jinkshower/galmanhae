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
	@DisplayName("장소 목록 조회")
	class Place {

		@Test
		@DisplayName("성공")
		@Sql("/test-initialize.sql")
		void success() {
			final ExtractableResponse<Response> result = getPlaces();

			assertThat(result.statusCode()).isEqualTo(200);
		}
	}

	private ExtractableResponse<Response> getPlaces() {
		return RestAssured.given().log().all()
			.when().get("/api/places")
			.then().log().all()
			.extract();
	}

	@Nested
	@DisplayName("장소 단일 조회")
	class PlaceNameAndCode {

		final Long validPlaceId = 1L;
		final Long invalidPlaceId = 0L;

		@Test
		@DisplayName("성공")
		@Sql("/test-initialize.sql")
		void success() {
			final ExtractableResponse<Response> result = getPlace(validPlaceId);

			assertAll(
				() -> assertThat(result.statusCode()).isEqualTo(200)
			);
		}

		@Test
		@DisplayName("없는 장소 입력시 실패한다")
		void fail_placeNotFound() {
			final ExtractableResponse<Response> result = getPlace(invalidPlaceId);

			assertThat(result.statusCode()).isEqualTo(404);
		}
	}

	private ExtractableResponse<Response> getPlace(final Long placeId) {
		return RestAssured.given().log().all()
			.pathParam("placeId", placeId)
			.when().get("/api/places/{placeId}")
			.then().log().all()
			.extract();
	}
}
