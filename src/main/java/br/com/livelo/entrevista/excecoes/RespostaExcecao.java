/**
 * 
 */
package br.com.livelo.entrevista.excecoes;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

/**
 * @author marcelo
 *
 */
@Getter
@Builder
public class RespostaExcecao {

	@Singular
	private List<RespostaMensagem> erros;
}

@Getter
@Builder
class RespostaMensagem {
	
	private final String codigo;

	private final String mensagem;
}
