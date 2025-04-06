package br.com.cdb.bancodigitalJPA.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
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
import br.com.cdb.bancodigitalJPA.exception.SubClasseDiferenteException;
import br.com.cdb.bancodigitalJPA.repository.ClienteRepository;
import br.com.cdb.bancodigitalJPA.repository.ContaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class ClienteService {

	@PersistenceContext
	private EntityManager entityManager;
	// classe do Jakarta

	// com o AutoWired eu não preciso me preocupar com a criação desse objeto
	// quando for construido o objeto do Repository, ele vai fazer um new quando precisar
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ContaRepository contaRepository;
	
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
			throw new SubClasseDiferenteException("Selecione o tipo de cliente");
		}

		cliente.setCpf(clienteDto.getCPF());
		cliente.setNome(clienteDto.getNome());
		cliente.setDataNascimento(clienteDto.getDataNascimento());
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
		contaRepository.deleteAll(cliente.getContas());
		
		clienteRepository.deleteById(id);
		return cliente;
	}

	@Transactional
	public Cliente atualizarCliente(Long id, ClienteDTO clienteDto) {
	    Cliente cliente = buscarClientePorId(id);

	    String tipoAtual = cliente.getClass().getSimpleName().toUpperCase().replace("CLIENTE", "");
	    String tipoNovo = clienteDto.getTipoDeCliente().toUpperCase();

	    if (!tipoAtual.equals(tipoNovo)) {
	        throw new SubClasseDiferenteException("Não é possível alterar o tipo de cliente.");
	    }

	    // Agora atualiza os dados normalmente
	    cliente.setNome(clienteDto.getNome());
	    cliente.setCpf(clienteDto.getCPF());
	    cliente.setDataNascimento(clienteDto.getDataNascimento());

	    Integer idade = Period.between(clienteDto.getDataNascimento(), LocalDate.now()).getYears();
	    if (idade < 18) {
	        throw new IdadeInsuficienteException("Apenas maiores de idade podem criar conta");
	    }

	    EnderecoResponse endereco = buscarEnderecoPorCep(clienteDto.getCep());

	    cliente.setCep(endereco.getCep());
	    cliente.setCidade(endereco.getCidade());
	    cliente.setEstado(endereco.getEstado());
	    cliente.setBairro(endereco.getBairro());
	    cliente.setRua(endereco.getRua());

	    return clienteRepository.save(cliente);
	}

	public List<ClienteResponse> getAllClientes() {
		List<Cliente> clientes = clienteRepository.findAll();
		return clientes.stream().map(this::converter).toList();
		// stream pra poder mexer na lista, map pega todos os elementos clientes e chama
		// o método converter
		// e o método converter, converte pra ClienteResponse.
	}

	public Cliente buscarClientePorId(Long id) {
		return clienteRepository.findById(id).orElseThrow(() -> new ObjetoNuloException("Cliente não encontrado"));
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

//	public Cliente atualizarTipoDeCliente(Cliente clienteAtual, String novoTipo) {
//
//		// se o cliente não fez nada pra mudar o tipo dele, só retorna o mesmo
//		if (clienteAtual.getTipoDeCliente().equalsIgnoreCase(novoTipo)) {
//			return clienteAtual;
//		}
//
//		clienteRepository.deleteById(clienteAtual.getId());
//		clienteRepository.flush(); // força a exclusão no banco
//		entityManager.clear(); // limpa o cache do Hibernate
//
//		Cliente novoCliente;
//
//		switch (novoTipo.toLowerCase()) {
//		case "comum":
//			novoCliente = new ClienteComum();
//			break;
//		case "super":
//			novoCliente = new ClienteSuper();
//			break;
//		case "premium":
//			novoCliente = new ClientePremium();
//			break;
//		default:
//			throw new IllegalArgumentException("Tipo de cliente inválido: " + novoTipo);
//		}
//
//		novoCliente.setNome(clienteAtual.getNome());
//		novoCliente.setCpf(clienteAtual.getCpf());
//		novoCliente.setDataNascimento(clienteAtual.getDataNascimento());
//		novoCliente.setCep(clienteAtual.getCep());
//		novoCliente.setCidade(clienteAtual.getCidade());
//		novoCliente.setEstado(clienteAtual.getEstado());
//		novoCliente.setBairro(clienteAtual.getBairro());
//		novoCliente.setRua(clienteAtual.getRua());
//		novoCliente.setContas(clienteAtual.getContas());
//
//		return clienteRepository.save(novoCliente);
//	}

}
