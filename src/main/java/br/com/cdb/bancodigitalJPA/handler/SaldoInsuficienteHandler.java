package br.com.cdb.bancodigitalJPA.handler;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.cdb.bancodigitalJPA.exception.SaldoInsuficienteException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class SaldoInsuficienteHandler {

	//o Map é como se fosse o Enum, ele trabalha no conceito de chave e valor, e o HashMap é a implementação
	//dessa interface Map. O HashMap organiza em "buckets", é uma forma mais rápida pra procurar valores
	
	@ExceptionHandler(SaldoInsuficienteException.class)
	public ResponseEntity<Map<String, Object>> handleSaldoInsuficienteException(SaldoInsuficienteException ex,
			HttpServletRequest request) { //usando essa classe pra ver o path do erro
		Map<String, Object> response = new HashMap<>();
		response.put("timestamp", LocalDate.now()); //pego o nome do erro e o valor dele, tipo um enum de erro
		response.put("status", HttpStatus.BAD_REQUEST.value());
		response.put("error", "Erro em Saldo");
		response.put("message", ex.getMessage()); 
		response.put("path", request.getRequestURI()); //é impossível prever onde o erro vai acontecer
		//então eu uso o request pra ser dinâmico
		
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
