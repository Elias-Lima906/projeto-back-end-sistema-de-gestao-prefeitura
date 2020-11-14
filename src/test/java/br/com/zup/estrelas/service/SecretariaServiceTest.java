package br.com.zup.estrelas.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.zup.estrelas.desafio.sistema.dto.MensagemDTO;
import br.com.zup.estrelas.desafio.sistema.dto.SecretariaDTO;
import br.com.zup.estrelas.desafio.sistema.enums.Area;
import br.com.zup.estrelas.desafio.sistema.repository.SecretariaRepository;
import br.com.zup.estrelas.desafio.sistema.service.SecretariaService;

@RunWith(MockitoJUnitRunner.class)
public class SecretariaServiceTest {

	@Mock
	SecretariaRepository secretariaRepository;

	@InjectMocks
	SecretariaService secretariaService;

	@Test
	public void deveAdicionarUmaSecretariaComSucesso() {
		SecretariaDTO secretariaDTO = this.criaSecretaria();

		Mockito.when(secretariaRepository.existsByArea(Area.EDUCACAO)).thenReturn(false);

		MensagemDTO mensagemRecebida = secretariaService.adicionaSecretaria(secretariaDTO);
		MensagemDTO mensagemEsperada = new MensagemDTO(
				"SECRETARIA DE(A) " + secretariaDTO.getArea() + " CRIADA COM SUCESSO!");

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);
	}

	@Test
	public void naoDeveAdicionarUmaSecretariaComSucesso() {
		SecretariaDTO secretariaDTO = this.criaSecretaria();

		Mockito.when(secretariaRepository.existsByArea(Area.EDUCACAO)).thenReturn(true);

		MensagemDTO mensagemRecebida = secretariaService.adicionaSecretaria(secretariaDTO);
		MensagemDTO mensagemEsperada = new MensagemDTO("JÁ EXISTE UMA SECRETARIA PARA ESTÁ ÁREA NA PREFEITURA!");

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);
	}

	private SecretariaDTO criaSecretaria() {
		SecretariaDTO secretariaDTO = new SecretariaDTO();

		secretariaDTO.setArea(Area.EDUCACAO);
		secretariaDTO.setEndereco("Rua oito, 40");
		secretariaDTO.setTelefone("19983792017");
		secretariaDTO.setOrcamentoProjetos(100000D);
		secretariaDTO.setOrcamentoFolha(100000D);
		secretariaDTO.setEmail("eliaslima900@hotmail.com");
		secretariaDTO.setSite("www.getMeIfYouCan.com.br");

		return secretariaDTO;
	}

}
