package br.com.livelo.entrevista.controladores;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.livelo.entrevista.dtos.ClienteDTO;
import br.com.livelo.entrevista.servicos.ClienteServico;
import io.micrometer.core.annotation.Timed;

/**
 * 
 * @author marcelo
 *
 */
@RestController
@RequestMapping("/clientes")
public class ClienteControlador {

	@Autowired
	private ClienteServico servico;
	
	@Timed("cliente.consultar.nome")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<ClienteDTO>> consultarClientePorNome(@RequestParam(name = "nome", required = true) String nome, Pageable paginacao) {
		
		Page<ClienteDTO> clientesPaginado = servico.obterClientePorNome(nome, paginacao);
		
		clientesPaginado.getContent().forEach(clienteDTO -> {
			adicionarLinks(clienteDTO);
		});

		return ResponseEntity.ok(clientesPaginado);
	}

	@Timed("cliente.salvar")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClienteDTO> salvar(@Valid @RequestBody final ClienteDTO dto) {

		ClienteDTO clienteSalvo = servico.salvar(dto);

		adicionarLinks(clienteSalvo);

		return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
	}

	@Timed("cliente.obter.id")
	@GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClienteDTO> obterClientePorId(@PathVariable(value = "id") Integer id) {

		ClienteDTO clienteDTO = servico.obterClientePorId(id);

		adicionarLinks(clienteDTO);

		return ResponseEntity.ok(clienteDTO);
	}

	@Timed("cliente.alterar")
	@PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClienteDTO> alterar(@RequestBody final ClienteDTO cliente,
			@PathVariable(value = "id") Integer id) {

		ClienteDTO clienteAlterado = servico.alterarNome(cliente.getNome(), id);
		
		adicionarLinks(clienteAlterado);

		return ResponseEntity.ok(clienteAlterado);
	}
	
	@Timed("cliente.excluir")
	@DeleteMapping(value = "{id}")
	public ResponseEntity<?> deletar(@PathVariable(value = "id") Integer id) {

		servico.deletar(id);
		
		return ResponseEntity.ok().build();
	}

	private void adicionarLinks(ClienteDTO clienteDTO) {

		Link linkSelf = linkTo(methodOn(ClienteControlador.class).obterClientePorId(clienteDTO.getId()))
				.withSelfRel();

		clienteDTO.add(linkSelf);
	}
}
