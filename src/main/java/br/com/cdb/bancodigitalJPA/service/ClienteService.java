package br.com.cdb.bancodigitalJPA.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigitalJPA.entity.Cliente;
import br.com.cdb.bancodigitalJPA.repository.ClienteRepository;

@Service
public class ClienteService {
	// o Repository funciona como um DAO, o Service se comunica com o Repository
	// agora, ele valida
	// e pede pro Repository trazer alguma coisa que tá guardado nele, como um banco
	// de dados

	// com o AutoWired eu não preciso me preocupar com a criação desse objeto
	 // quando for construido o objeto do Repository, ele vai fazer um new quando
	@Autowired	// precisar
	private ClienteRepository clienteRepository;

	// aqui então você precisa validar os campos primeiro
	// geralmente você usa o próprio Objeto, é uma boa prática:)
	public Cliente addCliente(String nome, String cpf) {
		Cliente cliente = new Cliente();
		cliente.setCpf(cpf);
		cliente.setNome(nome);
		if (clienteRepository.findByCpf(cliente.getCpf()).isPresent()) {
	        throw new IllegalArgumentException("Já existe um cliente com este CPF cadastrado.");
	    }
//		validarNome(nome);
//		validarCPF(cpf);

		return clienteRepository.save(cliente);
		// esse .save é um método do próprio JPA, por isso ele facilita TANTO a vida do programador
		// antes eu só preciso criar o objeto Cliente ( você só cria o objeto UMA VEZ) e passar os parâmetros
		// necessários pra salvar
	}



	//quando eu quero usar o método de buscar o cliente por ID, eu preciso usar o Optional, por que ele evita
	//que o método de erro, ele
	public Cliente removerCliente(Long id) {
		Optional<Cliente> clienteAchado = clienteRepository.findById(id);
		if(clienteAchado.isPresent()) {      //se eu consegui achar esse cliente, ai a gente remove ele
			clienteRepository.deleteById(id);
			return clienteAchado.get();
		}
		else { return null; }
		
	}
	
	public Cliente atualizarCliente(String nome, String cpf, Long id) {
		Optional<Cliente> clienteAchado = clienteRepository.findById(id);
		
		if(clienteAchado.isPresent()) {
			Cliente clienteAtualizar = clienteAchado.get();
			Optional<Cliente> clienteExistente = clienteRepository.findByCpf(clienteAtualizar.getCpf());
			if(clienteExistente.isPresent()) {
				throw new IllegalArgumentException("Já há um cliente cadastrado nesse CPF");
			}
			clienteAtualizar.setNome(nome);
			clienteAtualizar.setCpf(cpf);
			return clienteRepository.save(clienteAtualizar);
		}
		else { return null; }
		
	}
	
	public List<Cliente> getAllClientes() {
		return clienteRepository.findAll();
	}

	public Optional<Cliente> buscarClientePorId(Long id) {
		return clienteRepository.findById(id);
	}
	
	private void validarNome(String nome) {
		
		if(nome == null || nome.isEmpty()) {
			throw new IllegalArgumentException("O nome não pode estar vazio");
		}
		
		if(nome.length() < 2 || nome.length() > 100) {
			throw new IllegalArgumentException("O nome não pode ter menos de 2 ou mais de 100 letras!");
		}
		
		if(!nome.matches("^[A-Za-zÀ-ÿ\\s]+$")) {
			throw new IllegalArgumentException("O nome só deve conter letras ou espaços");
		}
	}
	
	private void validarCPF(String cpf) {
		
		if(cpf == null || cpf.isEmpty()) {
			throw new IllegalArgumentException("O cpf não pode estar vazio");
		}
		//aqui eu faço o replace pra tirar algum ponto/traço/espaço
		cpf = cpf.replaceAll("[^0-9]", "");
		// esse "\\d" só aceita números :)
		if(!cpf.matches("\\d{11}")) {
			throw new IllegalArgumentException("O cpf deve conter exatamente 11 digitos!");
		}
	}
}
