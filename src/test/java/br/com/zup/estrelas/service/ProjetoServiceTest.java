package br.com.zup.estrelas.service;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.zup.estrelas.desafio.sistema.dto.MensagemDTO;
import br.com.zup.estrelas.desafio.sistema.dto.ProjetoDTO;
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
		ProjetoDTO projetoDTO = this.criaProjeto();
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
		ProjetoDTO projetoDTO = this.criaProjeto();
		Secretaria secretaria = new Secretaria();
		secretaria.setOrcamentoProjetos(100000D);

		Mockito.when(secretariaRepository.existsById(projetoDTO.getIdSecretaria())).thenReturn(false);

		MensagemDTO mensagemRecebida = projetoService.adicionaNovoProjeto(projetoDTO);
		MensagemDTO mensagemEsperada = new MensagemDTO("A SECRETARIA EM QUESTÃO NÃO EXISTE!");

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);
	}

	@Test
	public void naoDeveAdicionarUmProjetoComSucessoCasoProjetoCusteMaisQueOrcamento() {
		ProjetoDTO projetoDTO = this.criaProjeto();
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

	private ProjetoDTO criaProjeto() {
		ProjetoDTO projetoDTO = new ProjetoDTO();

		projetoDTO.setNome("Ampliar Estrelas");
		projetoDTO.setDescricao("Este projeto visa ampliar o projeto estrelas.");
		projetoDTO.setCusto(25000D);
		projetoDTO.setIdSecretaria(11L);

		return projetoDTO;
	}
}
