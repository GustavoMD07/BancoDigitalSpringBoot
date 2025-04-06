package br.com.cdb.bancodigitalJPA.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cdb.bancodigitalJPA.DTO.SeguroDTO;
import br.com.cdb.bancodigitalJPA.DTO.SeguroResponse;
import br.com.cdb.bancodigitalJPA.entity.Cartao;
import br.com.cdb.bancodigitalJPA.entity.CartaoCredito;
import br.com.cdb.bancodigitalJPA.entity.ClientePremium;
import br.com.cdb.bancodigitalJPA.entity.Seguro;
import br.com.cdb.bancodigitalJPA.repository.SeguroRepository;

@Service
public class SeguroService {

	@Autowired
	private SeguroRepository seguroRepository;

	@Autowired
	private CartaoService cartaoService;

	public SeguroResponse contratarSeguro(SeguroDTO seguroDto) {
		
		Cartao cartao = cartaoService.buscarCartaoPorId(seguroDto.getCartaoId());
		if (!(cartao instanceof CartaoCredito)) {
		    throw new RuntimeException("Cartão informado não é de crédito!");
		}
		CartaoCredito cartaoCredito = (CartaoCredito) cartao;

		Seguro seguro = new Seguro();
		seguro.setCartao(cartaoCredito);
		seguro.setTipoDeSeguro(seguroDto.getTipoDeSeguro());
		seguro.setDataContratacao(LocalDate.now());
		seguro.setNumeroApolice(generatePolicyNumber());
		
		// regras de valor e descrição
		if (seguroDto.getTipoDeSeguro().equalsIgnoreCase("VIAGEM")) {

			if (cartao.getConta().getCliente() instanceof ClientePremium) {
				seguro.setValorApolice(0);
			} else {
				seguro.setValorApolice(50);
			}
			seguro.setDescricao("Cobertura viagem: ...");
		} else {
			seguro.setValorApolice(0);
			seguro.setDescricao("Cobertura fraude até R$5.000,00");
		}
		seguro.setAtivo(true);
		seguroRepository.save(seguro);
		return SeguroResponse.fromEntity(seguro);
	}
	
	public SeguroResponse getSeguro(Long id) {
	    Seguro seguro = seguroRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Seguro não encontrado"));
	    return SeguroResponse.fromEntity(seguro);
	}

	public List<SeguroResponse> listarSeguros() {
	    return seguroRepository.findAll()
	        .stream()
	        .map(SeguroResponse::fromEntity)
	        .toList();
	}

	public void cancelarSeguro(Long id) {
	    Seguro seguro = seguroRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Seguro não encontrado"));
	    seguro.setAtivo(false);
	    seguroRepository.save(seguro);
	}

	private String generatePolicyNumber() {
	    return "AP-" + System.currentTimeMillis();
	}
}
