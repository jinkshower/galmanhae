package hiyen.galmanhae.dataprocess.exception;

public class DataProcessCheckedException extends Exception {

	public DataProcessCheckedException(String message) {
		super(message);
	}

	public DataProcessCheckedException(String message, Throwable cause) {
		super(message, cause);
	}

	public static class FailFetchAPIException extends DataProcessCheckedException {

		public FailFetchAPIException(Throwable cause) {
			super("API를 호출하는데 실패했습니다.", cause);
		}
	}
}
