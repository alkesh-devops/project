package br.com.livelo.entrevista.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import br.com.livelo.entrevista.conversores.ClienteMapper;
import br.com.livelo.entrevista.dtos.ClienteDTO;
import br.com.livelo.entrevista.entidades.Cliente;
import br.com.livelo.entrevista.excecoes.BadRequestException;
import br.com.livelo.entrevista.excecoes.NotFoundException;
import br.com.livelo.entrevista.repositorios.ClienteRepositorio;

/**
 * Classe de exemplo que implementa um serviço da entidade {@link Cliente}
 * 
 * @author marcelo
 */
@Service
public class ClienteServico {
	
	@Autowired
	private ClienteRepositorio repositorio;
	
	@Autowired
	private ClienteMapper clienteMapper;
	
	@Transactional(readOnly=true)
	public Page<ClienteDTO> obterClientePorNome(String nome, Pageable paginacao) {
		
		Cliente clienteConsulta = new Cliente();
		clienteConsulta.setNome(nome);
		
		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAll()
			      .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains());
			
		Example<Cliente> example = Example.of(clienteConsulta, customExampleMatcher);
		
		Page<Cliente> todos = repositorio.findAll(example, paginacao);
		
		List<ClienteDTO> dtos = clienteMapper.converterListaEntidadeParaDTO(todos.getContent());
		
		return new PageImpl<ClienteDTO>(dtos, paginacao, todos.getTotalElements());
	}

	@Transactional(readOnly=true)
	public ClienteDTO obterClientePorId(Integer id) {
		
		Cliente cliente = obterPorId(id);
		
		return clienteMapper.converterEntidadeParaDTO(cliente);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public ClienteDTO salvar(ClienteDTO dto) {
		
		Cliente cliente = clienteMapper.converterDTOParaEntidade(dto);
		
		repositorio.save(cliente);
		
		return clienteMapper.converterEntidadeParaDTO(cliente);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public ClienteDTO alterarNome(String nome, Integer id) {
		
		if(StringUtils.isEmpty(nome)) {
			throw new BadRequestException();
		}
		
		Cliente cliente = obterPorId(id);
		
		cliente.setNome(nome);
		
		Cliente clienteSalvo = repositorio.save(cliente);
				
		return clienteMapper.converterEntidadeParaDTO(clienteSalvo);
	}
	
	public void deletar(Integer id) {
		
		repositorio.deleteById(id);
	}

	private Cliente obterPorId(Integer id) {
		
		return repositorio.findById(id)
				.orElseThrow(() -> new NotFoundException());
	}

	
}