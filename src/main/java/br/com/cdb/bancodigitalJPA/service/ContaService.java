package br.com.cdb.bancodigitalJPA.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigitalJPA.entity.Cliente;
import br.com.cdb.bancodigitalJPA.entity.Conta;
import br.com.cdb.bancodigitalJPA.repository.ClienteRepository;
import br.com.cdb.bancodigitalJPA.repository.ContaRepository;

@Service
public class ContaService {

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	// a conta puxa o cliente, e o cliente puxa o ID

	public Conta addConta(Conta conta) {
		// Recupera o cliente da conta
		Optional<Cliente> clienteEncontrado = clienteRepository.findById(conta.getCliente().getId());

		if (clienteEncontrado.isEmpty()) {
			throw new IllegalArgumentException("Cliente com o ID: " + conta.getCliente().getId() + " não encontrado!");
		}

		Cliente cliente = clienteEncontrado.get();

		if (cliente.getContas() != null && cliente.getContas().size() >= 2) {
			throw new IllegalStateException("O cliente já possui duas contas");
		}
		
		conta.setCliente(cliente);
		return contaRepository.save(conta);
	}

	public Conta removerConta(Long id) {
		Conta contaAchada = buscarContaPorId(id);
		if (contaAchada != null) {
			contaRepository.deleteById(id);
			return contaAchada;
		}
		return null;
	}

	public Conta buscarContaPorId(Long id) {
		Optional<Conta> conta = contaRepository.findById(id);
		if (conta.isPresent()) {
			return conta.get();
		} else {
			return null;
		}
	}

	public List<Conta> listarContas() {
		return contaRepository.findAll();
	}

	public Double verificarSaldo(Long id) {
		return buscarContaPorId(id).getSaldo();
	}

	public void transferir(Long origemid, Long destinoid, Double valor) {
		Conta origem = buscarContaPorId(origemid);
		Conta destino = buscarContaPorId(destinoid);

		if (valor > origem.getSaldo()) {
			throw new RuntimeException("Saldo insuficiente na conta de origem");
		}

		origem.setSaldo(origem.getSaldo() - valor);
		destino.setSaldo(destino.getSaldo() + valor);

		contaRepository.save(origem);
		contaRepository.save(destino); // atualizando as informações
	}

	public void pix(Long id, Double valor) {
		Conta conta = buscarContaPorId(id);
		if (valor > conta.getSaldo()) {
			throw new IllegalStateException("Saldo insuficiente");
		}
		conta.setSaldo(conta.getSaldo() - valor);
		contaRepository.save(conta);
	}

	public void deposito(Long id, Double valor) {
		Conta conta = buscarContaPorId(id);
		conta.setSaldo(conta.getSaldo() + valor);
		contaRepository.save(conta);
	}

	public void saque(Long id, Double valor) {
		Conta conta = buscarContaPorId(id);
		if (valor > conta.getSaldo()) {
			throw new IllegalStateException("Saldo insuficiente");
		}
		conta.setSaldo(conta.getSaldo() - valor);
		contaRepository.save(conta);
	}
}
