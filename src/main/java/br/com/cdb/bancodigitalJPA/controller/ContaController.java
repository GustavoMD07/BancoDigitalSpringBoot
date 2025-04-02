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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.bancodigitalJPA.DTO.ContaDTO;
import br.com.cdb.bancodigitalJPA.entity.Cliente;
import br.com.cdb.bancodigitalJPA.entity.Conta;
import br.com.cdb.bancodigitalJPA.entity.ContaCorrente;
import br.com.cdb.bancodigitalJPA.entity.ContaPoupanca;
import br.com.cdb.bancodigitalJPA.exception.ListaVaziaException;
import br.com.cdb.bancodigitalJPA.repository.ClienteRepository;
import br.com.cdb.bancodigitalJPA.service.ContaService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/contas")
public class ContaController {
	 
	@Autowired
	private ContaService contaService;
	@Autowired
	private ClienteRepository clienteRepository;
	
	
	@PostMapping("/add")
	public ResponseEntity<String> addConta(@RequestBody @Valid ContaDTO contaDto) {
		Optional<Cliente> clienteProcurado = clienteRepository.findById(contaDto.getClienteId());
		
		if(clienteProcurado.isEmpty()) {
			return new ResponseEntity<>("Cliente não encontrado", HttpStatus.NOT_FOUND);
		}
		Conta conta;
		
		if(contaDto.getTipoDeConta().equalsIgnoreCase("Corrente")) {
			conta = new ContaCorrente();
		}
		else if (contaDto.getTipoDeConta().equalsIgnoreCase("Poupança")) {
			conta = new ContaPoupanca();
		}
		else {
			return new ResponseEntity<>("Por favor, selecione um tipo de conta correto", HttpStatus.BAD_REQUEST);
		}
		
		conta.setSaldo(contaDto.getSaldo());
		conta.setCliente(clienteProcurado.get());
		contaService.addConta(conta);
		return new ResponseEntity<>("Conta "+ contaDto.getTipoDeConta() +" adicionada com sucesso", 
				HttpStatus.CREATED);
		
	}
	
	@DeleteMapping("/remove/{id}")
	public ResponseEntity<String> removerConta(@PathVariable Long id) {
		Conta contaRemovida = contaService.removerConta(id);
		
		if(contaRemovida != null) {
			return new ResponseEntity<>("Conta removida com sucesso", HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>("Não foi possível remover a conta de ID: " + id, HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/list/{id}")
	public ResponseEntity<Conta> buscarConta(@PathVariable Long id) {
		Conta contaProcurada = contaService.buscarContaPorId(id);
		
		if(contaProcurada != null) {
			return new ResponseEntity<>(contaProcurada, HttpStatus.FOUND);
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/listAll")
	public ResponseEntity<List<Conta>> listarContas() {
		List<Conta> contas = contaService.listarContas();
		if(contas.isEmpty()) {
			throw new ListaVaziaException("Não foram encontradas Contas");
		}
		return new ResponseEntity<List<Conta>>(contas, HttpStatus.OK);
	}
	
	@GetMapping("/saldo/{id}")
	public ResponseEntity<Double> verificarSaldo(@PathVariable Long id) {
		return new ResponseEntity<>(contaService.verificarSaldo(id), HttpStatus.OK);
	}
	
	@PostMapping("/transf/{id}")
	public ResponseEntity<String> transferencia(@PathVariable Long id, @RequestParam Long destinoid, @RequestParam Double valor){
		contaService.transferir(id, destinoid, valor);
		return new ResponseEntity<>("Transferência de R$ " + valor + " concluída com sucesso", HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/pix/{id}")
	public ResponseEntity<String> pix(@PathVariable Long id, @RequestParam Double valor) {
		contaService.pix(id, valor);
		return new ResponseEntity<>("Pix de R$ " + valor +" realizado com sucesso, caiu o green!", HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/deposito/{id}")
	public ResponseEntity<String> deposito(@PathVariable Long id, @RequestParam Double valor) {
		contaService.deposito(id, valor);
		return new ResponseEntity<>("Depósito de R$ " + valor + " realizado com sucesso", HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/saque/{id}")
	public ResponseEntity<String> saque(@PathVariable Long id, @RequestParam Double valor) {
		contaService.saque(id, valor);
		return new ResponseEntity<>("Saque de R$ " + valor + " realizado com sucesso", HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/manutencao/{id}")
	public ResponseEntity<String> taxaManutencao(@PathVariable Long id) {
		contaService.aplicarTaxaManutencao(id);
		return new ResponseEntity<>("Taxa de manutenção aplicada", HttpStatus.OK);
	}
	
	@PutMapping("/rendimento/{id}")
	public ResponseEntity<String> taxaRendimento(@PathVariable Long id) {
		contaService.aplicarRendimento(id);
		return new ResponseEntity<>("Taxa de rendimento aplicada", HttpStatus.OK);
	}
		

}
