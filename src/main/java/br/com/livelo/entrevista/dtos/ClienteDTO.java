package br.com.livelo.entrevista.dtos;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.springframework.hateoas.RepresentationModel;

import br.com.livelo.entrevista.dominios.SexoEnum;
import br.com.livelo.entrevista.validacoes.anotacoes.DataApos;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author marcelo
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ClienteDTO extends RepresentationModel<ClienteDTO> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4200716303653912778L;

	private Integer id;
	
	@NotEmpty
	private String nome;
	
	@NotNull
	private SexoEnum sexo;
	
	@DataApos(menorData = "1900-01-01")
	@PastOrPresent
	private LocalDate dataNascimento;

	@NotNull
	private CidadeDTO cidade;
	
	public Short getIdade() {
		
		if(this.dataNascimento == null) {
			return null;
		}
		
		Integer idade = Period.between(this.getDataNascimento(), LocalDate.now()).getYears();

		return idade.shortValue();
	}
}
