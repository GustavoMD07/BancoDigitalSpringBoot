package br.com.cdb.bancodigitalJPA.service;

import java.security.SecureRandom;
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
import br.com.cdb.bancodigitalJPA.exception.ObjetoNuloException;
import br.com.cdb.bancodigitalJPA.exception.StatusNegadoException;
import br.com.cdb.bancodigitalJPA.exception.SubClasseDiferenteException;
import br.com.cdb.bancodigitalJPA.repository.SeguroRepository;

@Service
public class SeguroService {

	private SecureRandom random = new SecureRandom();
	
	@Autowired
	private SeguroRepository seguroRepository;

	@Autowired
	private CartaoService cartaoService;

	public SeguroResponse contratarSeguro(SeguroDTO seguroDto) {
		
		Cartao cartao = cartaoService.buscarCartaoPorId(seguroDto.getCartaoId());
		
		if (!(cartao instanceof CartaoCredito)) {
		    throw new SubClasseDiferenteException("Seguros só podem ser aplicados a cartões de crédito!");
		}
		
		CartaoCredito cartaoCredito = (CartaoCredito) cartao;

		Seguro seguro = new Seguro();
		seguro.setCartao(cartaoCredito);
		seguro.setTipoDeSeguro(seguroDto.getTipoDeSeguro());
		seguro.setDataContratacao(LocalDate.now());
		seguro.setNumeroApolice(gerarNumApolice());
		
		// regras de valor e descrição
		if (seguroDto.getTipoDeSeguro().equalsIgnoreCase("viagem")) {

			if (cartao.getConta().getCliente() instanceof ClientePremium) {
				seguro.setValorApolice(0);
			} 
			else {
				seguro.setValorApolice(50);
			}
			seguro.setDescricao("Cobertura viagem assistência em caso de urgência médica, atrasos de voo, extravio de bagagem e etc");
		} 
		
		else if (seguroDto.getTipoDeSeguro().equalsIgnoreCase("fraude")) {
			seguro.setValorApolice(0);
			seguro.setDescricao("Cobertura fraude até R$5.000,00 para todos os tipos de cliente");
		}
		
		else {
			throw new SubClasseDiferenteException("Por favor, selecione um tipo válido de seguro: Viagem || Fraude");
		}
		
		seguro.setAtivo(true);
		seguroRepository.save(seguro);
		return SeguroResponse.fromEntity(seguro);
		 //aqui eu salvo o seguro Entity mas retorno o Response, pro usuário conseguir visualizar as informações
	}
	
	public SeguroResponse buscarSeguroPorId(Long id) {
	    Seguro seguro = seguroRepository.findById(id)
	        .orElseThrow(() -> new ObjetoNuloException("Seguro não encontrado"));
	    if(!seguro.isAtivo()) {
	    	throw new StatusNegadoException("Seguro desativado");
	    }
	    return SeguroResponse.fromEntity(seguro); // com esse orElseThrow, eu não preciso criar um Optional
	}

	public List<SeguroResponse> listarSeguros() {
	    
		
		return seguroRepository.findAll().stream().map(SeguroResponse::fromEntity).toList(); 
	    
	    //o stream "processa" todos os dados
	    //.map converte cada seguro em seguroResponse pelo fromEntity
	    //toList retorna os dados em uma lista
	}

	public void cancelarSeguro(Long id) {
	    Seguro seguro = seguroRepository.findById(id)
	        .orElseThrow(() -> new ObjetoNuloException("Seguro não encontrado"));
	    seguro.setAtivo(false);
	    seguroRepository.save(seguro);
	}

	private String gerarNumApolice() {
	    
		String num = "";
		
		for(int i = 0; i < 10; i++) {
			num += random.nextInt(9);
		}
		
		return "AP-" + num;
	}
}
