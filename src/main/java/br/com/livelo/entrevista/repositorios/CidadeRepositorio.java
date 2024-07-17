package br.com.livelo.entrevista.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.livelo.entrevista.entidades.Cidade;

@Repository
public interface CidadeRepositorio extends JpaRepository<Cidade, Integer> {

}
