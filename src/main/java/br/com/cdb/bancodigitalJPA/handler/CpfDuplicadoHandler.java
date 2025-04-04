package br.com.cdb.bancodigitalJPA.handler;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.cdb.bancodigitalJPA.exception.CpfDuplicadoException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class CpfDuplicadoHandler {

	@ExceptionHandler(CpfDuplicadoException.class)
	public ResponseEntity<Map<String, Object>> handleCpfDuplicadoException(CpfDuplicadoException ex, 
			HttpServletRequest request) {
		Map<String, Object> response = new HashMap<>();
		response.put("timestamp", LocalDate.now());
		response.put("Status", HttpStatus.CONFLICT.value());
		response.put("error", "Duplicação");
		response.put("message", ex.getMessage());
		response.put("path", request.getRequestURI());
		
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	
	}
}
