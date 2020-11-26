package br.com.zup.estrelas.desafio.sistema.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.estrelas.desafio.sistema.dto.AlteraProjetoDTO;
import br.com.zup.estrelas.desafio.sistema.dto.MensagemDTO;
import br.com.zup.estrelas.desafio.sistema.dto.ProjetoDTO;
import br.com.zup.estrelas.desafio.sistema.dto.TerminoProjetoDTO;
import br.com.zup.estrelas.desafio.sistema.entity.Projeto;
import br.com.zup.estrelas.desafio.sistema.service.ProjetoService;

@RestController
//FIXME: Elias, os recursos devem ser sempre mapeados no plural.
@RequestMapping("/projeto")
public class ProjetoController {

	@Autowired
	ProjetoService projetoService;

	@GetMapping(path = "/{idProjeto}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Optional<Projeto> buscapProjetoPorId(@PathVariable Long idProjeto) {
		return projetoService.buscapProjetoPorId(idProjeto);
	}

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Projeto> listaProjetos() {
		return projetoService.listaProjetos();
	}

	@PostMapping
	public MensagemDTO adicionaNovoProjeto(@RequestBody ProjetoDTO projetoDTO) {
		return projetoService.adicionaNovoProjeto(projetoDTO);
	}

	@PutMapping("/{idProjeto}")
	public MensagemDTO alteraInformacoesProjeto(@PathVariable Long idProjeto,
			@RequestBody AlteraProjetoDTO alteraProjetoDTO) {
		return projetoService.alteraInformacoesProjeto(idProjeto, alteraProjetoDTO);
	}

	@PutMapping(path = "/{idProjeto}/entrega")
	public MensagemDTO finalizaEntregaProjeto(@PathVariable Long idProjeto,
			@RequestBody TerminoProjetoDTO terminoProjetoDTO) {
		return projetoService.finalizaEntregaProjeto(idProjeto, terminoProjetoDTO);
	}

}
