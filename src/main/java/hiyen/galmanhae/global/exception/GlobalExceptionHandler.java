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
		DataException.NotFoundPlaceException.class
	})
	public ResponseEntity<ErrorResponse> handleNotFoundException(final RuntimeException exception) {
		String message = exception.getMessage();
		log.info("NotFoundException: {}", message, exception);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(message));
	}
}
