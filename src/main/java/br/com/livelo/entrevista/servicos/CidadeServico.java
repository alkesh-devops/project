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

import br.com.livelo.entrevista.conversores.CidadeMapper;
import br.com.livelo.entrevista.dtos.CidadeDTO;
import br.com.livelo.entrevista.entidades.Cidade;
import br.com.livelo.entrevista.repositorios.CidadeRepositorio;

/**
 * Classe de exemplo que implementa um serviço da entidade {@link Cidade}
 * 
 * @author marcelo
 */
@Service
public class CidadeServico {
	
	@Autowired
	private CidadeRepositorio repositorio;
	
	@Autowired
	private CidadeMapper cidadeMapper;
	
	@Transactional(readOnly=true)
	public Page<CidadeDTO> obterCidade(CidadeDTO cidadeDTO, Pageable paginacao) {
		
		Cidade cidadeConsulta = cidadeMapper.converterDTOParaEntidade(cidadeDTO);
			
		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAll()
			      .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains())
			      .withMatcher("estado", ExampleMatcher.GenericPropertyMatchers.contains());
			
		Example<Cidade> example = Example.of(cidadeConsulta, customExampleMatcher);
		
		Page<Cidade> todos = repositorio.findAll(example, paginacao);
		
		List<CidadeDTO> dtos = cidadeMapper.converterListaEntidadeParaDTO(todos.getContent());
		
		return new PageImpl<CidadeDTO>(dtos, paginacao, todos.getTotalElements());
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public CidadeDTO salvar(CidadeDTO cidadeDTO) {
		
		Cidade cidade = cidadeMapper.converterDTOParaEntidade(cidadeDTO);
		
		repositorio.save(cidade);
		
		return cidadeMapper.converterEntidadeParaDTO(cidade);
	}


	
}