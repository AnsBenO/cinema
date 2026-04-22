package ntt.beca.films.shared.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@ControllerAdvice
public class GlobalExceptionHandler {
	 @ExceptionHandler({NoHandlerFoundException.class,NoResourceFoundException.class})
	    public String handleNotFound(Exception ex, Model model) {
	        return "layouts/not-found";
	    }
}
