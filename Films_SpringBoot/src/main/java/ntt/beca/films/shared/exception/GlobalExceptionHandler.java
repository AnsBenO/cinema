package ntt.beca.films.shared.exception;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({ NoHandlerFoundException.class, NoResourceFoundException.class })
	public String handleNotFound(Exception ex, Model model) {
		return "layouts/not-found";
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
		Map<String, List<String>> fieldErrors = new LinkedHashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(fe -> fieldErrors.computeIfAbsent(fe.getField(), k -> new java.util.ArrayList<>())
						.add(fe.getDefaultMessage()));
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("status", HttpStatus.BAD_REQUEST.value());
		body.put("error", "Validation Failed");
		body.put("fieldErrors", fieldErrors);
		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	@ResponseBody
	public ResponseEntity<Map<String, String>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(Map.of("error", ex.getMessage()));
	}

}
