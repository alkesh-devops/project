package br.com.livelo.entrevista.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author marcelo
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CidadeDTO extends RepresentationModel<CidadeDTO> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5730276310505753624L;
	
	private Integer id;

	@NotEmpty
	private String nome;
	
	@NotEmpty
	private String estado;
}
