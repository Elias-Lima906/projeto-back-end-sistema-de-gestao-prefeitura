package br.com.zup.estrelas.service;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;

import br.com.zup.estrelas.desafio.sistema.dto.MensagemDTO;
import br.com.zup.estrelas.desafio.sistema.dto.SecretariaDTO;
import br.com.zup.estrelas.desafio.sistema.entity.Secretaria;
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
	public void naoDeveAdicionarUmaSecretariaComSucessoCasoJaExista() {
		SecretariaDTO secretariaDTO = this.criaSecretaria();

		Mockito.when(secretariaRepository.existsByArea(Area.EDUCACAO)).thenReturn(true);

		MensagemDTO mensagemRecebida = secretariaService.adicionaSecretaria(secretariaDTO);
		MensagemDTO mensagemEsperada = new MensagemDTO("JÁ EXISTE UMA SECRETARIA PARA ESTÁ ÁREA NA PREFEITURA!");

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);
	}

	@Test
	public void deveAlterarUmaSecretariaComSucesso() {
		SecretariaDTO secretariaDTO = criaSecretaria();
		Secretaria secretaria = new Secretaria();

		BeanUtils.copyProperties(secretariaDTO, secretaria);

		Mockito.when(secretariaRepository.existsById(1L)).thenReturn(true);
		Mockito.when(secretariaRepository.findById(1L)).thenReturn(Optional.of(secretaria));

		Assert.assertFalse(secretaria.getArea() != secretariaDTO.getArea()
				&& secretariaRepository.existsByArea(secretariaDTO.getArea()));

		MensagemDTO mensagemRecebida = secretariaService.alteraSecretaria(1L, secretariaDTO);
		MensagemDTO mensagemEsperada = new MensagemDTO("ALTERAÇÃO APROVADA, E EFETUADA NA SECRETARIA!");

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);
	}

	@Test
	public void naoDeveAlterarUmaSecretariaComSucessoCasoAreaAlteradaJaExista() {
		SecretariaDTO secretariaDTO = criaSecretaria();
		Secretaria secretaria = new Secretaria();
		Secretaria secretariaDois = new Secretaria();

		BeanUtils.copyProperties(secretariaDTO, secretaria);

		secretariaDois.setArea(Area.SAUDE);
		secretariaDTO.setArea(Area.SAUDE);

		Mockito.when(secretariaRepository.existsById(1L)).thenReturn(true);
		Mockito.when(secretariaRepository.findById(1L)).thenReturn(Optional.of(secretaria));
		Mockito.when(secretariaRepository.existsByArea(secretariaDTO.getArea())).thenReturn(true);

		Assert.assertTrue(secretaria.getArea() != secretariaDTO.getArea());

		MensagemDTO mensagemRecebida = secretariaService.alteraSecretaria(1L, secretariaDTO);
		MensagemDTO mensagemEsperada = new MensagemDTO("JÁ EXISTE UMA SECRETARIA PARA ESTÁ ÁREA NA PREFEITURA!");

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);
	}

	@Test
	public void naoDeveAlterarUmaSecretariaComSucessoCasoNaoExista() {
		SecretariaDTO secretariaDTO = criaSecretaria();
		Secretaria secretaria = new Secretaria();

		BeanUtils.copyProperties(secretariaDTO, secretaria);

		Mockito.when(secretariaRepository.existsById(1L)).thenReturn(false);

		MensagemDTO mensagemRecebida = secretariaService.alteraSecretaria(1L, secretariaDTO);
		MensagemDTO mensagemEsperada = new MensagemDTO("A SECRETARIA EM QEUSTÃO NÃO EXISTE!");

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);
	}

	@Test
	public void deveRemoverComSucesso() {
		SecretariaDTO secretariaDTO = criaSecretaria();
		Secretaria secretaria = new Secretaria();

		BeanUtils.copyProperties(secretariaDTO, secretaria);

		Mockito.when(secretariaRepository.existsById(1L)).thenReturn(true);
		Mockito.when(secretariaRepository.findById(1L)).thenReturn(Optional.of(secretaria));

		MensagemDTO mensagemRecebida = secretariaService.removeSecretaria(1L);
		MensagemDTO mensagemEsperada = new MensagemDTO("SECRETARIA DESCONTINUADA COM SUCESSO!");

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);
	}

	@Test
	public void naoDeveRemoverComSucessoCasoNaoExista() {

		Mockito.when(secretariaRepository.existsById(1L)).thenReturn(false);

		MensagemDTO mensagemRecebida = secretariaService.removeSecretaria(1L);
		MensagemDTO mensagemEsperada = new MensagemDTO("ESSA SECRETARIA AINDA NÃO FOI CRIADA!");

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
