package hiyen.galmanhae.global.exception;

import hiyen.galmanhae.data.exception.DataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = {
		DataException.NotFoundPlaceException.class,
		DataException.NotFoundCongestionException.class,
		DataException.NotFoundWeatherException.class
	})
	public ResponseEntity<ErrorResponse> handleNotFoundException(final DataException exception) {
		final String message = exception.getMessage();
		log.info("NotFoundException: {}", message, exception);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(message));
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException(final RuntimeException exception) {
		final String message = exception.getMessage();
		log.error("RuntimeException: {}", message, exception);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(message));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
		final String message = "An unexpected error occurred. Please try again later.";
		log.error("Unhandled Exception: {}", exception.getMessage(), exception);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(message));
	}

}
