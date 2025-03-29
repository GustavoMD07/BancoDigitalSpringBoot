package br.com.cdb.bancodigitalJPA.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigitalJPA.DTO.ClienteDTO;
import br.com.cdb.bancodigitalJPA.entity.Cliente;
import br.com.cdb.bancodigitalJPA.entity.ClienteComum;
import br.com.cdb.bancodigitalJPA.entity.ClientePremium;
import br.com.cdb.bancodigitalJPA.entity.ClienteSuper;
import br.com.cdb.bancodigitalJPA.exception.CpfDuplicadoException;
import br.com.cdb.bancodigitalJPA.exception.IdadeInsuficienteException;
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
	public Cliente addCliente(ClienteDTO clienteDto) {
		//pego a data de nascimento, depois pego a data de hoje e comparo
		LocalDate dataNascimento = clienteDto.getDataNascimento();
		LocalDate hoje = LocalDate.now();
		
		Integer idade = Period.between(dataNascimento, hoje).getYears();
		
		if (idade < 18) {
			throw new IdadeInsuficienteException("O cliente deve ter 18 anos ou mais para criar a conta!");
		}
		
		Cliente cliente;
		
		if(clienteDto.getTipoDeCliente().equalsIgnoreCase("Comum")) {
			cliente = new ClienteComum();
		}
		
		else if(clienteDto.getTipoDeCliente().equalsIgnoreCase("Super")) {
			cliente = new ClienteSuper();
		}
		
		else if(clienteDto.getTipoDeCliente().equalsIgnoreCase("Premium")) {
			cliente = new ClientePremium();
		}
		else {
			throw new IllegalStateException("Selecione o tipo de cliente");
		}
		
		cliente.setCpf(clienteDto.getCPF());
		cliente.setNome(clienteDto.getNome());
		cliente.setIdade(idade);
		if (clienteRepository.findByCpf(cliente.getCpf()).isPresent()) {
			throw new CpfDuplicadoException("Já existe um cliente com este CPF cadastrado.");
	    }


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
	
	public Cliente atualizarCliente(String nome, String cpf, LocalDate dataNascimento, Long id) {
		Optional<Cliente> clienteAchado = clienteRepository.findById(id);
		
		if(clienteAchado.isPresent()) {
			Cliente clienteAtualizar = clienteAchado.get();
			
			Optional<Cliente> clienteExistente = clienteRepository.findByCpf(clienteAtualizar.getCpf());
			
			if(clienteExistente.isPresent()) {
				throw new CpfDuplicadoException("Já há um cliente cadastrado nesse CPF");
			}
			
			LocalDate hoje = LocalDate.now();
			Integer idade = Period.between(dataNascimento, hoje).getYears();
			if(idade < 18) {
				throw new IdadeInsuficienteException("Apenas maiores de idade podem criar conta");
			}
		
			clienteAtualizar.setNome(nome);
			clienteAtualizar.setCpf(cpf);
			clienteAtualizar.setIdade(idade);
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
	
}
