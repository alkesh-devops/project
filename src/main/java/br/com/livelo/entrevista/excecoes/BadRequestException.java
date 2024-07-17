/**
 * 
 */
package br.com.livelo.entrevista.excecoes;

import org.springframework.http.HttpStatus;

/**
 * @author marcelo
 *
 */
public class BadRequestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -424966475496013022L;
	
	private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
	
	/**
	 * Construtor
	 */
    public BadRequestException() {
        super("MSG002");
    }
    
    public HttpStatus gteHttpStatus() {
    	return this.httpStatus;
    }
}
