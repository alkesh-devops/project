package br.com.livelo.entrevista.controladores;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.livelo.entrevista.dtos.CidadeDTO;
import br.com.livelo.entrevista.servicos.CidadeServico;
import io.micrometer.core.annotation.Timed;

/**
 * 
 * @author marcelo
 *
 */
@RestController
@RequestMapping("/cidades")
public class CidadeControlador {

	@Autowired
	private CidadeServico servico;
	
	@Timed("cidade.consultar.nome.estado")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<CidadeDTO>> consultarCidades(CidadeDTO cidadeDTO, Pageable paginacao) {
		
		Page<CidadeDTO> cidadesPaginadas = servico.obterCidade(cidadeDTO, paginacao);
		
		return ResponseEntity.ok(cidadesPaginadas);
	}

	@Timed("cidade.salvar")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CidadeDTO> salvar(@Valid @RequestBody final CidadeDTO dto) {

		CidadeDTO cidadeSalva = servico.salvar(dto);

		return ResponseEntity.status(HttpStatus.CREATED).body(cidadeSalva);
	}

}
