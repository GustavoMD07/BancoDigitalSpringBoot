package br.com.cdb.bancodigitalJPA.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.cdb.bancodigitalJPA.DTO.ClienteDTO;
import br.com.cdb.bancodigitalJPA.DTO.ClienteResponse;
import br.com.cdb.bancodigitalJPA.DTO.EnderecoResponse;
import br.com.cdb.bancodigitalJPA.entity.Cliente;
import br.com.cdb.bancodigitalJPA.entity.ClienteComum;
import br.com.cdb.bancodigitalJPA.entity.ClientePremium;
import br.com.cdb.bancodigitalJPA.entity.ClienteSuper;
import br.com.cdb.bancodigitalJPA.exception.ApiBloqueadaException;
import br.com.cdb.bancodigitalJPA.exception.CpfDuplicadoException;
import br.com.cdb.bancodigitalJPA.exception.IdadeInsuficienteException;
import br.com.cdb.bancodigitalJPA.exception.ObjetoNuloException;
import br.com.cdb.bancodigitalJPA.repository.ClienteRepository;

@Service
public class ClienteService {
	// o Repository funciona como um DAO, o Service se comunica com o Repository
	// agora, ele valida
	// e pede pro Repository trazer alguma coisa que tá guardado nele, como um banco
	// de dados

	// com o AutoWired eu não preciso me preocupar com a criação desse objeto
	// quando for construido o objeto do Repository, ele vai fazer um new quando
	@Autowired // precisar
	private ClienteRepository clienteRepository;
	private final RestTemplate restTemplate = new RestTemplate();
	// RestTemplate é um "navegador", eu uso ele pra poder me comunicar com alguma
	// API que seja externa ao meu sistemaz

	// aqui então você precisa validar os campos primeiro
	// geralmente você usa o próprio Objeto, é uma boa prática:)
	public Cliente addCliente(ClienteDTO clienteDto) {
		// pego a data de nascimento, depois pego a data de hoje e comparo
		LocalDate dataNascimento = clienteDto.getDataNascimento();
		LocalDate hoje = LocalDate.now();

		Integer idade = Period.between(dataNascimento, hoje).getYears();

		if (idade < 18) {
			throw new IdadeInsuficienteException("O cliente deve ter 18 anos ou mais para criar a conta!");
		}

		Cliente cliente;

		if (clienteDto.getTipoDeCliente().equalsIgnoreCase("Comum")) {
			cliente = new ClienteComum();
		}

		else if (clienteDto.getTipoDeCliente().equalsIgnoreCase("Super")) {
			cliente = new ClienteSuper();
		}

		else if (clienteDto.getTipoDeCliente().equalsIgnoreCase("Premium")) {
			cliente = new ClientePremium();
		} else {
			throw new IllegalStateException("Selecione o tipo de cliente");
		}

		cliente.setCpf(clienteDto.getCPF());
		cliente.setNome(clienteDto.getNome());
		cliente.setIdade(idade);
		if (clienteRepository.findByCpf(cliente.getCpf()).isPresent()) {
			throw new CpfDuplicadoException("Já existe um cliente com este CPF cadastrado.");
		}

		EnderecoResponse endereco = buscarEnderecoPorCep(clienteDto.getCep());

		cliente.setCep(endereco.getCep());
		cliente.setCidade(endereco.getCidade());
		cliente.setEstado(endereco.getEstado());
		cliente.setBairro(endereco.getBairro());
		cliente.setRua(endereco.getRua());

		return clienteRepository.save(cliente);
		// esse .save é um método do próprio JPA, por isso ele facilita TANTO a vida do
		// programador
		// antes eu só preciso criar o objeto Cliente ( você só cria o objeto UMA VEZ) e
		// passar os parâmetros
		// necessários pra salvar
	}

	// quando eu quero usar o método de buscar o cliente por ID, eu preciso usar o
	// Optional, por que ele evita
	// que o método de erro, ele
	public Cliente removerCliente(Long id) {
		Cliente cliente = buscarClientePorId(id);
		clienteRepository.deleteById(id);
		return cliente;
	}

	public Cliente atualizarCliente(String nome, String cpf, LocalDate dataNascimento, Long id) {
		Cliente cliente = buscarClientePorId(id);

		Cliente clienteAtualizar = cliente;

		if (clienteAtualizar == null) {
			throw new ObjetoNuloException("Não foi encontrado nenhum cliente");
		}

		LocalDate hoje = LocalDate.now();
		Integer idade = Period.between(dataNascimento, hoje).getYears();
		if (idade < 18) {
			throw new IdadeInsuficienteException("Apenas maiores de idade podem criar conta");
		}

		clienteAtualizar.setNome(nome);

		clienteAtualizar.setCpf(cpf);
		if (clienteRepository.findByCpf(clienteAtualizar.getCpf()).isPresent()) {
			throw new CpfDuplicadoException("Já existe um cliente com este CPF cadastrado.");
		}

		clienteAtualizar.setIdade(idade);
		return clienteRepository.save(clienteAtualizar);
	}

	public List<ClienteResponse> getAllClientes() {
		List<Cliente> clientes = clienteRepository.findAll();
		return clientes.stream().map(this::converter).collect(Collectors.toList());
		// stream pra poder mexer na lista, map pega todos os elementos clientes e chama
		// o método converter
		// e o método converter, converte pra ClienteResponse. O collect cria uma nova
		// lista de ClienteResponse
		// e a lista é retornada
	}

	public Cliente buscarClientePorId(Long id) {
		return clienteRepository.findById(id).orElseThrow(() -> 
			new ObjetoNuloException("Cliente não encontrado"));
	}

	private EnderecoResponse buscarEnderecoPorCep(String cep) {
		try {
			String url = "https://brasilapi.com.br/api/cep/v1/" + cep;
			ResponseEntity<EnderecoResponse> response = restTemplate.getForEntity(url, EnderecoResponse.class);
			// pego o corpo do EnderecoResponse, a url é onde eu quero pegar os dados pra
			// armazenar no EnderecoResponse

			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				return response.getBody();
				// dos HTTP, se o retorno começa com 2, eu posso continuar, então to verificando
				// se eu consigo
				// também verifico se o corpo é nulo, se for, tem algo errado
			} else {
				throw new ObjetoNuloException("CEP inválido");
			}
		} catch (Exception e) {
			throw new ApiBloqueadaException("Erro ao integrar com a APi" + e.getMessage());
		}
	}

	private ClienteResponse converter(Cliente cliente) {
		EnderecoResponse endereco = new EnderecoResponse(cliente.getCep(), cliente.getEstado(), cliente.getCidade(),
				cliente.getRua(), cliente.getBairro());

		return new ClienteResponse(cliente.getNome(), cliente.getCpf(), cliente.getTipoDeCliente(), cliente.getId(),
				cliente.getIdade(), endereco, cliente.getContas());
	}

}
