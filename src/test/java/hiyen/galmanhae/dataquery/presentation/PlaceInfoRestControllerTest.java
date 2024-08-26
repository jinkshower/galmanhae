package hiyen.galmanhae.dataquery.presentation;

import static org.assertj.core.api.Assertions.*;

import hiyen.galmanhae.common.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

class PlaceInfoRestControllerTest extends AcceptanceTest {

	@Nested
	@DisplayName("장소 목록 조회")
	class PlaceInfo {

		@Test
		@DisplayName("성공")
		@Sql("/test-initialize.sql")
		void success() {
			final ExtractableResponse<Response> result = getPlaceInfos();

			assertThat(result.statusCode()).isEqualTo(200);
		}
	}

	private ExtractableResponse<Response> getPlaceInfos() {
		return RestAssured.given().log().all()
			.when().get("/api/place-infos")
			.then().log().all()
			.extract();
	}
}
