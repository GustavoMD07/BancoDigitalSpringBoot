package br.com.cdb.bancodigitalJPA.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.PrimaryKeyJoinColumn;


@Entity
@PrimaryKeyJoinColumn(name = "idCartao")
public class CartaoCredito extends Cartao {
	
	//qualquer coisa, usa o JsonIgnore
	//o double primitivo não tava permitindo que o objeto fosse nulo
	
	private double limiteCredito;
	
	private double fatura;
	
	public CartaoCredito() {
		
	}
	
	public double getLimiteCredito() {
		
		return limiteCredito;
	}

	public void setLimiteCredito(double limiteCredito) {
		this.limiteCredito = limiteCredito;
	}

	public double getFatura() {
		return fatura;
	}

	public void setFatura(double fatura) {
		this.fatura = fatura;
	}
	
	
	@PrePersist
    @PreUpdate
    //uso esse método e notações pra ele calcular o limite antes de colocar a entidade no banco
    //assim o H2 não fica zerado
    public void calcularLimiteAntesDeSalvar() {
        if (this.limiteCredito == 0.0 && getConta() != null && getConta().getCliente() != null) {
            this.limiteCredito = getConta().getCliente().getLimiteCredito();
        }
    }

}
