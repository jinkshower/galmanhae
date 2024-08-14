package hiyen.galmanhae.dataprocess.exception;

public class DataProcessingException extends RuntimeException {

	public DataProcessingException(String message) {
		super(message);
	}

	public DataProcessingException(String message, Throwable cause) {
		super(message, cause);
	}

	public static class FailReadingFileException extends DataProcessingException {

		public FailReadingFileException(Throwable cause) {
			super("파일을 읽는데 실패했습니다.", cause);
		}
	}
}
