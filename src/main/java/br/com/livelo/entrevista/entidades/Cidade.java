package br.com.livelo.entrevista.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author marcelo
 */
@Data
@Entity
@Table(name="TB_CIDADE")
public class Cidade implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6279177161486360394L;

	@Id 
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="NOME", length = 300)
	private String nome;
	
	@Column(name="ESTADO")
	private String estado;
}
