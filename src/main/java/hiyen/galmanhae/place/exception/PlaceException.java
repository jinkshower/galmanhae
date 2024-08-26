package hiyen.galmanhae.place.exception;

public class PlaceException extends RuntimeException {

	public PlaceException(String message) {
		super(message);
	}

	public PlaceException(String message, Throwable cause) {
		super(message, cause);
	}

	public static class NotFoundPlaceException extends PlaceException {

		public NotFoundPlaceException(final String placeName) {
			super("장소를 찾을 수 없습니다. 장소 이름: " + placeName);
		}
	}
}
