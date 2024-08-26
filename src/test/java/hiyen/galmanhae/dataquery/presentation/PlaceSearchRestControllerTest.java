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

class PlaceSearchRestControllerTest extends AcceptanceTest {

	@Nested
	@DisplayName("장소 검색")
	class Search {

		final String validKeyword = "강남";

		@DisplayName("성공")
		@Test
		@Sql("/test-initialize.sql")
		void success() {
			final ExtractableResponse<Response> response = search(validKeyword);

			assertThat(response.statusCode()).isEqualTo(200);
		}
	}

	private ExtractableResponse<Response> search(final String keyword) {
		return RestAssured.given().log().all()
			.pathParam("keyword", keyword)
			.when().get("/api/search/{keyword}")
			.then().log().all()
			.extract();
	}
}
