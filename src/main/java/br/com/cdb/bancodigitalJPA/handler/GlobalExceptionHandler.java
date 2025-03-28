package br.com.cdb.bancodigitalJPA.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


//que nem o Denilson faloud RestController, só que esse é Advice, ou seja, toda vez que acontece algum erro
//eu to falando que é essa classe aqui que vai intervir em alguma coisa que ocorre no Controller
@RestControllerAdvice
public class GlobalExceptionHandler {

	//esse parâmetro é automaticamente retornado quando alguma validação do Bean Validation dá errado
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		
		ex.getBindingResult().getAllErrors().forEach(error -> { //aq ele pega todos os erros que vierem
			
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
}
