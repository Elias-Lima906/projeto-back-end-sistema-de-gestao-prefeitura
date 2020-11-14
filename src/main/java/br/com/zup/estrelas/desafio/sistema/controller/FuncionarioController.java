package br.com.zup.estrelas.desafio.sistema.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.estrelas.desafio.sistema.dto.FuncionarioDTO;
import br.com.zup.estrelas.desafio.sistema.dto.MensagemDTO;
import br.com.zup.estrelas.desafio.sistema.entity.Funcionario;
import br.com.zup.estrelas.desafio.sistema.service.FuncionarioService;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

	@Autowired
	FuncionarioService funcionarioService;

	@GetMapping(path = "/{idFuncionario}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Optional<Funcionario> buscaFuncionarioPorId(@PathVariable Long idFuncionario) {
		return funcionarioService.buscaFuncionarioPorId(idFuncionario);
	}

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Funcionario> listaFuncionarios() {
		return funcionarioService.listaFuncionarios();
	}

	@PostMapping
	public MensagemDTO adicionaNovoFuncionario(@RequestBody FuncionarioDTO funcionarioDTO) {
		return funcionarioService.adicionaNovoFuncionario(funcionarioDTO);
	}

	@PutMapping(path = "/{idFuncionario}")
	public MensagemDTO alteraInformacoesFuncionario(@PathVariable Long idFuncionario,
			@RequestBody FuncionarioDTO funcionarioDTO) {
		return funcionarioService.alteraInformacoesFuncionario(idFuncionario, funcionarioDTO);
	}

	@DeleteMapping(path = "/{idFuncionario}")
	public MensagemDTO removeFuncionario(@PathVariable Long idFuncionario) {
		return funcionarioService.removeFuncionario(idFuncionario);
	}

}