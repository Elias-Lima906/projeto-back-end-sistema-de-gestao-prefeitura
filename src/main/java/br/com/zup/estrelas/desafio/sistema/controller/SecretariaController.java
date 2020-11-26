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

import br.com.zup.estrelas.desafio.sistema.dto.MensagemDTO;
import br.com.zup.estrelas.desafio.sistema.dto.SecretariaDTO;
import br.com.zup.estrelas.desafio.sistema.entity.Secretaria;
import br.com.zup.estrelas.desafio.sistema.service.SecretariaService;

@RestController
//FIXME: Elias, os recursos devem ser sempre mapeados no plural.
@RequestMapping("/secretaria")
public class SecretariaController {

	@Autowired
	SecretariaService secretariaService;

	@GetMapping(path = "/{idSecretaria}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Optional<Secretaria> buscaSecretariaPorId(@PathVariable Long idSecretaria) {
		return secretariaService.buscaSecretariaPorId(idSecretaria);
	}

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Secretaria> listaTodasAsSecretarias() {
		return secretariaService.listaTodasAsSecretarias();
	}

	@PostMapping
	public MensagemDTO adicionaSecretaria(@RequestBody SecretariaDTO secretariaDTO) {
		return secretariaService.adicionaSecretaria(secretariaDTO);
	}

	@PutMapping(path = "/{idSecretaria}")
	public MensagemDTO alteraSecretaria(@PathVariable Long idSecretaria, @RequestBody SecretariaDTO secretariaDTO) {
		return secretariaService.alteraSecretaria(idSecretaria, secretariaDTO);
	}

	@DeleteMapping(path = "/{idSecretaria}")
	public MensagemDTO romoveSecretaria(@PathVariable Long idSecretaria) {
		return secretariaService.removeSecretaria(idSecretaria);
	}

}
