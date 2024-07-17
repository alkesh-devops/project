package br.com.livelo.entrevista.excecoes;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.livelo.entrevista.excecoes.RespostaExcecao.RespostaExcecaoBuilder;

/**
 * Classe que manipula exceções do tipo {@link MethodArgumentNotValidException},
 * {@link ConstraintViolationException}, {@link BadRequestException},
 * {@link NotFoundException}, {@link Exception} e {@link RuntimeException}.
 * 
 * O retorno em JSON seguirá o seguinte contrato: <br>
 * 
 * - Mais de um erro:<br>
 * { "erros": [ { "codigo": "XXXX", "mensagem": "mensagem de erro" }, {
 * "codigo": "YYYY", "mensagem": "mensagem de erro" } ] }
 * 
 * @author marcelo
 * 
 */
@RestControllerAdvice
public class TratamentoExcecoes extends ResponseEntityExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(TratamentoExcecoes.class);

	@Autowired
	private Validator validator;

	@Autowired
	ObjectMapper mapper;

	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		logger.error(e.getMessage(), e);

		Set<ConstraintViolation<Object>> violacoes = validator.validate(e.getBindingResult().getTarget());

		RespostaExcecaoBuilder builder = obterBuilderMensagensViolacao(
				converterViolacoesObjectParaGenerica(violacoes));

		return new ResponseEntity<Object>(builder.build(), HttpStatus.BAD_REQUEST);
	}

	protected ResponseEntity<Object> handleBindException(BindException e, HttpHeaders headers, HttpStatus status,
			WebRequest request) {

		logger.error(e.getMessage(), e);

		Set<ConstraintViolation<Object>> violacoes = validator.validate(e.getTarget());

		RespostaExcecaoBuilder builder = obterBuilderMensagensViolacao(
				converterViolacoesObjectParaGenerica(violacoes));

		return new ResponseEntity<Object>(builder.build(), HttpStatus.BAD_REQUEST);
	}

	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		logger.error(e.getMessage(), e);
		
		RespostaMensagem msg = RespostaMensagem.builder().codigo("MSGERRO007").mensagem("Erro no parse do JSON").build();

		RespostaExcecao resposta = RespostaExcecao.builder().erro(msg).build();

		return new ResponseEntity<>(resposta, HttpStatus.BAD_REQUEST);
	}

	protected ResponseEntity<Object> handleExceptionInternal(Exception e, @Nullable Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		logger.error(e.getMessage(), e);

		return super.handleExceptionInternal(e, body, headers, status, request);
	}
	
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException e, 
			HttpHeaders headers, HttpStatus status, WebRequest request){
		
		logger.error(e.getMessage(), e);
		
		RespostaMensagem msg = RespostaMensagem.builder().codigo("MSGERRO006").mensagem(e.getMessage()).build();

		RespostaExcecao resposta = RespostaExcecao.builder().erro(msg).build();

		return new ResponseEntity<>(resposta, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	protected ResponseEntity<RespostaExcecao> handleConstraintViolationException(ConstraintViolationException e,
			WebRequest request) {

		logger.error(e.getMessage(), e);

		Set<ConstraintViolation<?>> violacoes = e.getConstraintViolations();

		RespostaExcecaoBuilder builder = obterBuilderMensagensViolacao(violacoes);

		return new ResponseEntity<RespostaExcecao>(builder.build(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	protected ResponseEntity<RespostaExcecao> handleRuntimeException(RuntimeException e, WebRequest request) {

		logger.error(e.getMessage(), e);

		RespostaMensagem msg = RespostaMensagem.builder().codigo("MSGERRO005").mensagem(e.getMessage()).build();

		RespostaExcecao resposta = RespostaExcecao.builder().erro(msg).build();

		return new ResponseEntity<RespostaExcecao>(resposta, HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@ExceptionHandler
	protected ResponseEntity<RespostaExcecao> handleBadRequestException(BadRequestException e,
			WebRequest request) {

		logger.error(e.getMessage(), e);

		RespostaMensagem msg = RespostaMensagem.builder().codigo("MSGERRO004").mensagem(e.getMessage()).build();

		RespostaExcecao resposta = RespostaExcecao.builder().erro(msg).build();

		return new ResponseEntity<RespostaExcecao>(resposta, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	protected ResponseEntity<RespostaExcecao> handleNotFoundException(NotFoundException e,
			WebRequest request) {

		logger.error(e.getMessage(), e);

		RespostaMensagem msg = RespostaMensagem.builder().codigo("MSGERRO003").mensagem("Entidade ou recurso não encontrado").build();

		RespostaExcecao resposta = RespostaExcecao.builder().erro(msg).build();

		return new ResponseEntity<RespostaExcecao>(resposta, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	protected ResponseEntity<RespostaExcecao> handleDataIntegrityViolationException(DataIntegrityViolationException e, 
			WebRequest request){
		
		logger.error(e.getMessage(), e);

		RespostaMensagem msg = RespostaMensagem.builder().codigo("MSGERRO001").mensagem(e.getMessage()).build();

		RespostaExcecao resposta = RespostaExcecao.builder().erro(msg).build();

		return new ResponseEntity<RespostaExcecao>(resposta, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	protected ResponseEntity<RespostaExcecao> handleEmptyResultDataAccessException(EmptyResultDataAccessException e, 
			WebRequest request){
		
		logger.error(e.getMessage(), e);

		RespostaMensagem msg = RespostaMensagem.builder().codigo("MSGERRO002").mensagem(e.getMessage()).build();

		RespostaExcecao resposta = RespostaExcecao.builder().erro(msg).build();

		return new ResponseEntity<RespostaExcecao>(resposta, HttpStatus.BAD_REQUEST);
	}

	private RespostaExcecao.RespostaExcecaoBuilder obterBuilderMensagensViolacao(
			Set<ConstraintViolation<?>> violacoes) {

		RespostaExcecaoBuilder builder = RespostaExcecao.builder();

		for (ConstraintViolation<?> violacao : violacoes) {
			
			RespostaMensagem msg = RespostaMensagem.builder().codigo("MSGERRO008").mensagem(violacao.getMessage()).build();

			builder.erro(msg);
		}

		return builder;
	}

	private Set<ConstraintViolation<?>> converterViolacoesObjectParaGenerica(
			Set<ConstraintViolation<Object>> violacoes) {

		Set<ConstraintViolation<?>> violacoesGenericas = new HashSet<>();

		violacoes.forEach(violacao -> violacoesGenericas.add(violacao));

		return violacoesGenericas;
	}

}



