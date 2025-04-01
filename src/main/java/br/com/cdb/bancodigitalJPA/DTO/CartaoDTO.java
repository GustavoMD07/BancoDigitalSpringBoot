package br.com.cdb.bancodigitalJPA.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartaoDTO {

	@NotNull(message = "O ID da conta não pode estar vazio")
	private Long contaId;
	
	@NotBlank(message = "A senha não pode estar vazia")
	@Size(min = 8, max = 100, message = "A senha precisa ter no minímo 8 caracteres")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!_@#-]).+$", 
	message = "A senha deve conter pelo menos uma letra maíscula, um número e um caracter especial "
			+ "(- _ ! * @ #)")
	private String senha;
	
	@NotBlank(message = "Selecione o tipo de cartão: Crédito || Débito")
	private String tipoDeCartao;
}
