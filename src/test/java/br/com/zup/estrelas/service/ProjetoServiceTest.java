package br.com.zup.estrelas.service;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.zup.estrelas.desafio.sistema.dto.AlteraProjetoDTO;
import br.com.zup.estrelas.desafio.sistema.dto.MensagemDTO;
import br.com.zup.estrelas.desafio.sistema.dto.ProjetoDTO;
import br.com.zup.estrelas.desafio.sistema.dto.TerminoProjetoDTO;
import br.com.zup.estrelas.desafio.sistema.entity.Projeto;
import br.com.zup.estrelas.desafio.sistema.entity.Secretaria;
import br.com.zup.estrelas.desafio.sistema.repository.ProjetoRepository;
import br.com.zup.estrelas.desafio.sistema.repository.SecretariaRepository;
import br.com.zup.estrelas.desafio.sistema.service.ProjetoService;

@RunWith(MockitoJUnitRunner.class)
public class ProjetoServiceTest {

	@Mock
	ProjetoRepository projetoRepository;

	@Mock
	SecretariaRepository secretariaRepository;

	@InjectMocks
	ProjetoService projetoService;

	@Test
	public void deveAdicionarUmProjetoComSucesso() {
		ProjetoDTO projetoDTO = this.criaProjetoDTO();
		Secretaria secretaria = new Secretaria();
		secretaria.setOrcamentoProjetos(100000D);

		Mockito.when(secretariaRepository.existsById(projetoDTO.getIdSecretaria())).thenReturn(true);
		Mockito.when(secretariaRepository.findById(projetoDTO.getIdSecretaria())).thenReturn(Optional.of(secretaria));

		MensagemDTO mensagemRecebida = projetoService.adicionaNovoProjeto(projetoDTO);
		MensagemDTO mensagemEsperada = new MensagemDTO(
				"O PROJETO FOI APROVADO COM SUCESSO E PASSARÁ PARA O PROCESSO DE CRIAÇÃO EM BREVE!");

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);
	}

	@Test
	public void naoDeveAdicionarUmProjetoComSucessoCasoSecretariaNaoExista() {
		ProjetoDTO projetoDTO = this.criaProjetoDTO();
		Secretaria secretaria = new Secretaria();
		secretaria.setOrcamentoProjetos(100000D);

		Mockito.when(secretariaRepository.existsById(projetoDTO.getIdSecretaria())).thenReturn(false);

		MensagemDTO mensagemRecebida = projetoService.adicionaNovoProjeto(projetoDTO);
		MensagemDTO mensagemEsperada = new MensagemDTO("A SECRETARIA EM QUESTÃO NÃO EXISTE!");

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);
	}

	@Test
	public void naoDeveAdicionarUmProjetoComSucessoCasoProjetoCusteMaisQueOrcamento() {
		ProjetoDTO projetoDTO = this.criaProjetoDTO();
		Secretaria secretaria = new Secretaria();
		secretaria.setOrcamentoProjetos(20000D);

		Mockito.when(secretariaRepository.existsById(projetoDTO.getIdSecretaria())).thenReturn(true);
		Mockito.when(secretariaRepository.findById(projetoDTO.getIdSecretaria())).thenReturn(Optional.of(secretaria));

		Assert.assertTrue(secretaria.getOrcamentoProjetos() < projetoDTO.getCusto());

		MensagemDTO mensagemRecebida = projetoService.adicionaNovoProjeto(projetoDTO);
		MensagemDTO mensagemEsperada = new MensagemDTO(
				"O VALOR DO PROJETO ESTA ACIMA DO QUE A SECRETARIA PODE COBRIR!");

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);
	}

	@Test
	public void deveAlterarComSucesso() {
		Projeto projeto = new Projeto();
		AlteraProjetoDTO alteraProjeto = new AlteraProjetoDTO();

		alteraProjeto.setDescricao("Olá, td bem?");

		Mockito.when(projetoRepository.existsById(1L)).thenReturn(true);
		Mockito.when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

		MensagemDTO mensagemRecebida = projetoService.alteraInformacoesProjeto(1L, alteraProjeto);
		MensagemDTO mensagemEsperada = new MensagemDTO("DESCRIÇÃO DO PROJETO ALTERADA COM SUCESSO!");

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);
	}

	@Test
	public void naoDeveAlterarComSucessoCasoNaoExista() {
		AlteraProjetoDTO alteraProjeto = new AlteraProjetoDTO();

		Mockito.when(projetoRepository.existsById(1L)).thenReturn(false);

		MensagemDTO mensagemRecebida = projetoService.alteraInformacoesProjeto(1L, alteraProjeto);
		MensagemDTO mensagemEsperada = new MensagemDTO("O PROJETO EM QUESTÃO NÃO FOI ENCONTRADO!");

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);
	}

	@Test
	public void naoDeveAlterarComSucessoCasoDescricaoEstejaVazia() {
		AlteraProjetoDTO alteraProjeto = new AlteraProjetoDTO();

		alteraProjeto.setDescricao("   ");

		Mockito.when(projetoRepository.existsById(1L)).thenReturn(true);
		Assert.assertTrue(alteraProjeto.getDescricao().isBlank());

		MensagemDTO mensagemRecebida = projetoService.alteraInformacoesProjeto(1L, alteraProjeto);
		MensagemDTO mensagemEsperada = new MensagemDTO(
				"A DESCRIÇÃO DO PROJETO DEVE CONTER ALGUM CONTEUDO, NÃO PODE ESTAR VAZIA OU COM APENAS PONTOS!");

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);
	}

	@Test
	public void deveFinalizarProjetoComSucesso() {
		TerminoProjetoDTO terminoProjetoDTO = new TerminoProjetoDTO();
		Projeto projeto = new Projeto();

		projeto.setDataInicio(LocalDate.of(2019, 9, 15));

		terminoProjetoDTO.setDataEntrega(LocalDate.of(2020, 2, 25));

		Mockito.when(projetoRepository.existsById(1L)).thenReturn(true);
		Mockito.when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

		Assert.assertFalse(projeto.getDataInicio().isAfter(terminoProjetoDTO.getDataEntrega()));

		MensagemDTO mensagemRecebida = projetoService.finalizaEntregaProjeto(1L, terminoProjetoDTO);
		MensagemDTO mensagemEsperada = new MensagemDTO("PROJETO FINALIZADO COM SUCESSO!");

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);
	}

	@Test
	public void naoDeveFinalizarProjetoComSucessoCasoNaoExista() {
		TerminoProjetoDTO terminoProjetoDTO = new TerminoProjetoDTO();

		Mockito.when(projetoRepository.existsById(1L)).thenReturn(false);

		MensagemDTO mensagemRecebida = projetoService.finalizaEntregaProjeto(1L, terminoProjetoDTO);
		MensagemDTO mensagemEsperada = new MensagemDTO("O PROJETO EM QUESTÃO NÃO FOI ENCONTRADO!");

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);
	}

	@Test
	public void naoDeveFinalizarProjetoComSucessoCasoDataInicialForMaiorQueFinal() {
		TerminoProjetoDTO terminoProjetoDTO = new TerminoProjetoDTO();
		Projeto projeto = new Projeto();

		projeto.setDataInicio(LocalDate.of(2020, 9, 15));

		terminoProjetoDTO.setDataEntrega(LocalDate.of(2020, 2, 25));

		Mockito.when(projetoRepository.existsById(1L)).thenReturn(true);
		Mockito.when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));

		Assert.assertTrue(projeto.getDataInicio().isAfter(terminoProjetoDTO.getDataEntrega()));

		MensagemDTO mensagemRecebida = projetoService.finalizaEntregaProjeto(1L, terminoProjetoDTO);
		MensagemDTO mensagemEsperada = new MensagemDTO(
				"A DATA DE TERMINO DEVE SER MAIOR QUE A DATA INICIAL DO PROJETO! -> VOCÊ COMEÇOU NO DIA "
						+ projeto.getDataInicio());

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);
	}

	private ProjetoDTO criaProjetoDTO() {
		ProjetoDTO projetoDTO = new ProjetoDTO();

		projetoDTO.setNome("Ampliar Estrelas");
		projetoDTO.setDescricao("Este projeto visa ampliar o projeto estrelas.");
		projetoDTO.setCusto(25000D);
		projetoDTO.setIdSecretaria(11L);

		return projetoDTO;
	}
}
