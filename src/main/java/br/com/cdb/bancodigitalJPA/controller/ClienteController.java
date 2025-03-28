package br.com.cdb.bancodigitalJPA.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigitalJPA.entity.Cliente;
import br.com.cdb.bancodigitalJPA.service.ClienteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired //mesma coisa, usando o AutoWired pra não me preocupar em instânciar a classe
	private ClienteService clienteService;
	
	//esse ResponseEntity eu uso pra dar alguma resposta a quem fez a requisição
	//é mais pra informar o que tá acontecendo
	@PostMapping("/add")
	public ResponseEntity<String> addCliente(@RequestBody @Valid Cliente cliente) {
		
		
		Cliente clienteAdicionado = clienteService.addCliente(cliente.getNome(), cliente.getCpf());
		
		if(clienteAdicionado != null) {
			return new ResponseEntity<>("Cliente " + cliente.getNome() + " adicionado com sucesso",
				HttpStatus.CREATED);
			//esse HttpStatus são aqueles números tipo "404 - not found", "200 - ok", lembra do site dos gatos
		}
		
		else {
			return new ResponseEntity<>("Cliente " + cliente.getNome() + " não foi adicionado ao sistema, "
			+ "nome ou CPF inválidos", HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@DeleteMapping("/remove/{id}")
	public ResponseEntity<String> removeCliente(@PathVariable Long id) {
		
		Cliente clienteRemovido = clienteService.removerCliente(id);
		
		if(clienteRemovido != null) {
			return new ResponseEntity<>("Cliente " + clienteRemovido.getNome() +
			" ID: " + clienteRemovido.getId() + " removido com sucesso", HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>("Não foi possível encontrar o cliente ", HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/atualizar/{id}")
	public ResponseEntity<String> atualizarCliente(@PathVariable Long id,  @RequestBody @Valid Cliente cliente) {
		
		Cliente clienteAtualizado = clienteService.atualizarCliente(cliente.getNome(), cliente.getCpf(), id);
		if(clienteAtualizado != null) {
			return new ResponseEntity<>("Cliente " + cliente.getNome() + " atualizado com sucesso!", HttpStatus.ACCEPTED);
		}
		else {
			return new ResponseEntity<>("Não foi possível atualizar o cliente ", HttpStatus.NOT_MODIFIED);
		}
	}
	
	@GetMapping("/listAll")
	public ResponseEntity<List<Cliente>> listarClientes() {
		List<Cliente> clientes = clienteService.getAllClientes();
		return new ResponseEntity<List<Cliente>>(clientes, HttpStatus.OK);
	}
	
	@GetMapping("/list/{id}")			//com o PathVariable, eu mostro que o LongId vai ser o que o usuário escreve
	public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Long id) {
		
		Optional<Cliente> clienteProcurado = clienteService.buscarClientePorId(id);
		if( clienteProcurado.isPresent() ) {
			return new ResponseEntity<>( clienteProcurado.get(), HttpStatus.FOUND);
		}
		
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		//o próprio JPA já retorna o cliente com o ID, então eu não preciso usar um List, se eu usasse, eu estaria
		//fazendo algo desnecessário
	}
	
	
}
