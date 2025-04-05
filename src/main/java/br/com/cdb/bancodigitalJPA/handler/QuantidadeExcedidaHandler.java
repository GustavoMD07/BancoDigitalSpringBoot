package br.com.cdb.bancodigitalJPA.handler;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import br.com.cdb.bancodigitalJPA.exception.QuantidadeExcedidaException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class QuantidadeExcedidaHandler {

	@ExceptionHandler(QuantidadeExcedidaException.class)
	public ResponseEntity<Map<String, Object>> handleQuantidadeExcedidaException(QuantidadeExcedidaException ex, HttpServletRequest request) {
		Map<String, Object> response = new HashMap<>();
		response.put("timelapse", LocalDate.now());
		response.put("status", HttpStatus.BAD_REQUEST.value());
		response.put("error", "Quantidade excedida");
		response.put("message", ex.getMessage());
		response.put("path", request.getRequestURI());
		
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
