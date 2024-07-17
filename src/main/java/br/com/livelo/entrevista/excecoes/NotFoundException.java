/**
 * 
 */
package br.com.livelo.entrevista.excecoes;

import org.springframework.http.HttpStatus;

/**
 * @author marcelo
 *
 */
public class NotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -424966475496013022L;

	private HttpStatus httpStatus = HttpStatus.NOT_FOUND;

	/**
	 * Construtor
	 */
	public NotFoundException() {
		super("MSG003");
	}

	public HttpStatus gteHttpStatus() {
		return this.httpStatus;
	}

}
