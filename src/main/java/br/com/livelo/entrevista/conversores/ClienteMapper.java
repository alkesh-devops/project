package br.com.livelo.entrevista.conversores;

import java.util.List;

import org.mapstruct.Mapper;

import br.com.livelo.entrevista.dtos.ClienteDTO;
import br.com.livelo.entrevista.entidades.Cliente;

/**
 * Conversor entre entidade {@link Cliente} e dto {@link ClienteDTO}
 * 
 * @author marcelo
 */
@Mapper(componentModel = "spring")
public interface ClienteMapper {

	ClienteDTO converterEntidadeParaDTO(Cliente entidade);
	
	Cliente converterDTOParaEntidade(ClienteDTO dto);

	List<ClienteDTO> converterListaEntidadeParaDTO(List<Cliente> content);
	
}
