package hiyen.galmanhae.data.exception;

public class NotFoundException extends RuntimeException {

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public static class NotFoundPlaceException extends NotFoundException {

		public NotFoundPlaceException(final Long placeId) {
			super("장소를 찾을 수 없습니다. 장소 이름: " + placeId);
		}
	}

	public static class NotFoundWeatherException extends NotFoundException {

		public NotFoundWeatherException(final Long id) {
			super("날씨 정보를 찾을 수 없습니다. 장소 식별자: " + id);
		}
	}

	public static class NotFoundCongestionException extends NotFoundException {

		public NotFoundCongestionException(final Long id) {
			super("혼잡도 정보를 찾을 수 없습니다. 장소 식별자: " + id);
		}
	}
}
