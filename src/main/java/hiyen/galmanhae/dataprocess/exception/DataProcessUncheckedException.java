package hiyen.galmanhae.dataprocess.exception;

public class DataProcessUncheckedException extends RuntimeException {

	public DataProcessUncheckedException(String message) {
		super(message);
	}

	public DataProcessUncheckedException(String message, Throwable cause) {
		super(message, cause);
	}

	public static class FailReadingFileException extends DataProcessUncheckedException {

		public FailReadingFileException(Throwable cause) {
			super("파일을 읽는데 실패했습니다.", cause);
		}
	}

	public static class FailFetchAPIUncheckedException extends DataProcessUncheckedException {

		public FailFetchAPIUncheckedException(Throwable cause) {
			super("API를 호출하는데 실패했습니다.", cause);
		}
	}

	public static class InvalidDataException extends DataProcessUncheckedException {

		public InvalidDataException() {
			super("데이터가 올바르지 않습니다");
		}
	}
}
