package br.com.livelo.entrevista.conversores;

import java.util.List;

import org.mapstruct.Mapper;

import br.com.livelo.entrevista.dtos.CidadeDTO;
import br.com.livelo.entrevista.entidades.Cidade;

/**
 * Conversor entre entidade {@link Cidade} e dto {@link CidadeDTO}
 * 
 * @author marcelo
 */
@Mapper(componentModel = "spring")
public interface CidadeMapper {


	CidadeDTO converterEntidadeParaDTO(Cidade cidade);
	
	
	Cidade converterDTOParaEntidade(CidadeDTO dto);

	List<CidadeDTO> converterListaEntidadeParaDTO(List<Cidade> content);
}
