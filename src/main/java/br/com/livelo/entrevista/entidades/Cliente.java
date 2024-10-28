package br.com.livelo.entrevista.entidades;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.livelo.entrevista.dominios.SexoEnum;
import lombok.Data;

/**
 * @author marcelo
 */
@Data
@Entity
@Table(name="TB_CLIENTE")
public class Cliente implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6279177161486360394L;

	@Id 
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="NOME_COMPLETO",length = 200)
	private String nome;
	
	@Enumerated(EnumType.STRING)
	@Column(name="SEXO")
	private SexoEnum sexo;
	
	@Column(name="DATA_NASCIMENTO")
	private LocalDate dataNascimento;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CIDADE", updatable=false)
	private Cidade cidade;
}
