package br.com.zup.estrelas.desafio.sistema.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.zup.estrelas.desafio.sistema.entity.Secretaria;
import br.com.zup.estrelas.desafio.sistema.enums.Area;

@Repository
public interface SecretariaRepository extends CrudRepository<Secretaria, Long>{

	boolean existsByArea(Area area);

	Secretaria findByArea(Area area);
}
