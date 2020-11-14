package br.com.zup.estrelas.desafio.sistema.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.zup.estrelas.desafio.sistema.entity.Funcionario;

@Repository
public interface FuncionarioRepository extends CrudRepository<Funcionario, Long> {

	boolean existsByCpf(String cpf);

	Funcionario findByCpf(String cpf);
}
