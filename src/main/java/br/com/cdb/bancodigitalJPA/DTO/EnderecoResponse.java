package br.com.cdb.bancodigitalJPA.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoResponse {
	
	private String cep;			
	
	@JsonProperty("state")		//a api retorna "state", mas eu quero que seja "estado"
	private String estado;		//então eu pego a chave "state" e mudo ela pra chave "estado", mas o valor é igual
	
	@JsonProperty("city")
	private String cidade;
	
	@JsonProperty("street")
	private String rua;
	
	@JsonProperty("neighborhood")
	private String bairro;
}
