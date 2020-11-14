package br.com.zup.estrelas.desafio.sistema.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.zup.estrelas.desafio.sistema.dto.AlteraProjetoDTO;
import br.com.zup.estrelas.desafio.sistema.dto.MensagemDTO;
import br.com.zup.estrelas.desafio.sistema.dto.ProjetoDTO;
import br.com.zup.estrelas.desafio.sistema.dto.TerminoProjetoDTO;
import br.com.zup.estrelas.desafio.sistema.entity.Projeto;
import br.com.zup.estrelas.desafio.sistema.entity.Secretaria;
import br.com.zup.estrelas.desafio.sistema.repository.ProjetoRepository;
import br.com.zup.estrelas.desafio.sistema.repository.SecretariaRepository;

@Service
public class ProjetoService {

	@Autowired
	ProjetoRepository projetoRepository;

	@Autowired
	SecretariaRepository secretariaRepository;

	public Optional<Projeto> buscapProjetoPorId(Long idProjeto) {
		if (!projetoRepository.existsById(idProjeto)) {
			return Optional.empty();
		}

		return projetoRepository.findById(idProjeto);
	}

	public List<Projeto> listaProjetos() {

		return (List<Projeto>) projetoRepository.findAll();
	}

	public MensagemDTO adicionaNovoProjeto(ProjetoDTO projetoDTO) {

		if (!secretariaRepository.existsById(projetoDTO.getIdSecretaria())) {
			return new MensagemDTO("A SECRETARIA EM QUESTÃO NÃO EXISTE!");
		}

		Secretaria secretaria = secretariaRepository.findById(projetoDTO.getIdSecretaria()).get();

		if (secretaria.getOrcamentoProjetos() < projetoDTO.getCusto()) {
			return new MensagemDTO("O VALOR DO PROJETO ESTA ACIMA DO QUE A SECRETARIA PODE COBRIR!");
		}

		MensagemDTO criadoComSucesso = this.criaProjeto(projetoDTO);

		return criadoComSucesso;
	}

	public MensagemDTO alteraInformacoesProjeto(Long idProjeto, AlteraProjetoDTO alteraProjetoDTO) {
		if (!projetoRepository.existsById(idProjeto)) {
			return new MensagemDTO("O PROJETO EM QUESTÃO NÃO FOI ENCONTRADO!");
		}

		MensagemDTO alterdoComSucesso = this.alteraProjeto(idProjeto, alteraProjetoDTO);

		return alterdoComSucesso;
	}

	public MensagemDTO finalizaEntregaProjeto(Long idProjeto, TerminoProjetoDTO terminoProjetoDTO) {
		if (!projetoRepository.existsById(idProjeto)) {
			return new MensagemDTO("O PROJETO EM QUESTÃO NÃO FOI ENCONTRADO!");
		}

		Projeto projeto = projetoRepository.findById(idProjeto).get();

		if (projeto.getDataInicio().isAfter(terminoProjetoDTO.getDataEntrega())) {
			return new MensagemDTO("A DATA DE TERMINO DEVE SER MAIOR QUE A DATA INICIAL DO PROJETO! -> VOCÊ COMEÇOU NO DIA " + projeto.getDataInicio());
		}

		MensagemDTO finalizadoComSucesso = this.finalizaProjeto(idProjeto, terminoProjetoDTO);

		return finalizadoComSucesso;
	}

	private MensagemDTO criaProjeto(ProjetoDTO projetoDTO) {
		Projeto projeto = new Projeto();

		BeanUtils.copyProperties(projetoDTO, projeto);
		projeto.setDataInicio(LocalDate.now());
		projeto.setDataEntrega(null);
		projeto.setConcluido(false);

		projetoRepository.save(projeto);

		return new MensagemDTO("O PROJETO FOI APROVADO COM SUCESSO E PASSARÁ PARA O PROCESSO DE CRIAÇÃO EM BREVE!");
	}

	private MensagemDTO alteraProjeto(Long idProjeto, AlteraProjetoDTO alteraProjetoDTO) {
		Projeto projeto = projetoRepository.findById(idProjeto).get();

		projeto.setDescricao(alteraProjetoDTO.getDescricao());

		projetoRepository.save(projeto);

		return new MensagemDTO("DESCRIÇÃO DO PROJETO ALTERADA COM SUCESSO!");
	}

	private MensagemDTO finalizaProjeto(Long idProjeto, TerminoProjetoDTO terminoProjetoDTO) {
		Projeto projeto = projetoRepository.findById(idProjeto).get();

		projeto.setDataEntrega(terminoProjetoDTO.getDataEntrega());
		projeto.setConcluido(true);

		projetoRepository.save(projeto);

		return new MensagemDTO("PROJETO FINALIZADO COM SUCESSO!");

	}
}
