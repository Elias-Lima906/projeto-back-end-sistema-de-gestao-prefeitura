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

import br.com.zup.estrelas.desafio.sistema.dto.FuncionarioDTO;
import br.com.zup.estrelas.desafio.sistema.dto.MensagemDTO;
import br.com.zup.estrelas.desafio.sistema.entity.Secretaria;
import br.com.zup.estrelas.desafio.sistema.repository.FuncionarioRepository;
import br.com.zup.estrelas.desafio.sistema.repository.SecretariaRepository;
import br.com.zup.estrelas.desafio.sistema.service.FuncionarioService;

@RunWith(MockitoJUnitRunner.class)
public class FuncionarioServiceTest {

	@Mock
	FuncionarioRepository funcionarioRepository;

	@Mock
	SecretariaRepository secretariaRepository;

	@InjectMocks
	FuncionarioService funcionarioService;

	@Test
	public void deveAdicionarUmFuncionarioComSucesso() {
		FuncionarioDTO funcionarioDTO = this.criaFuncionario();
		Secretaria secretaria = new Secretaria();

		secretaria.setOrcamentoFolha(100000D);

		Mockito.when(funcionarioRepository.existsByCpf(funcionarioDTO.getCpf())).thenReturn(false);
		Mockito.when(secretariaRepository.existsById(funcionarioDTO.getIdSecretaria())).thenReturn(true);
		Mockito.when(secretariaRepository.findById(funcionarioDTO.getIdSecretaria()))
				.thenReturn(Optional.of(secretaria));
		Mockito.when(secretariaRepository.save(Mockito.any())).thenReturn(secretaria);

		MensagemDTO mensagemRecebida = funcionarioService.adicionaNovoFuncionario(funcionarioDTO);
		MensagemDTO mensagemEsperada = new MensagemDTO("FUNCIONARIO DEVIDAMENTE ADICIONADO AO BANCO DE DADOS");

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);
	}

	@Test
	public void naoDeveAdicionarUmFuncionarioComSucessoCasoExista() {
		FuncionarioDTO funcionarioDTO = this.criaFuncionario();
		Secretaria secretaria = new Secretaria();

		secretaria.setOrcamentoFolha(100000D);

		Mockito.when(funcionarioRepository.existsByCpf(funcionarioDTO.getCpf())).thenReturn(true);

		MensagemDTO mensagemRecebida = funcionarioService.adicionaNovoFuncionario(funcionarioDTO);
		MensagemDTO mensagemEsperada = new MensagemDTO("ESSE FUNCIONARIO JÁ ESTÁ CADASTRADO NO NOSSO SISTEMA!");

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);
	}

	@Test
	public void naoDeveAdicionarUmFuncionarioComSucessoCasoASecretariaNaoExista() {
		FuncionarioDTO funcionarioDTO = this.criaFuncionario();
		Secretaria secretaria = new Secretaria();

		secretaria.setOrcamentoFolha(100000D);

		Mockito.when(funcionarioRepository.existsByCpf(funcionarioDTO.getCpf())).thenReturn(false);
		Mockito.when(secretariaRepository.existsById(funcionarioDTO.getIdSecretaria())).thenReturn(false);

		MensagemDTO mensagemRecebida = funcionarioService.adicionaNovoFuncionario(funcionarioDTO);
		MensagemDTO mensagemEsperada = new MensagemDTO(
				"A SECRETARIA EM QUESTÃO NÃO EXISTE, TENTE EXCAIXA-LO EM OUTRA SECRETARIA!");

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);
	}

	@Test
	public void deveAdicionarUmFuncionarioComSucessoCasoSalarioForMaiorQueOrcamento() {
		FuncionarioDTO funcionarioDTO = this.criaFuncionario();
		Secretaria secretaria = new Secretaria();

		secretaria.setOrcamentoFolha(2000D);

		Mockito.when(funcionarioRepository.existsByCpf(funcionarioDTO.getCpf())).thenReturn(false);
		Mockito.when(secretariaRepository.existsById(funcionarioDTO.getIdSecretaria())).thenReturn(true);
		Mockito.when(secretariaRepository.findById(funcionarioDTO.getIdSecretaria()))
				.thenReturn(Optional.of(secretaria));

		Assert.assertTrue(secretaria.getOrcamentoFolha() < funcionarioDTO.getSalario());

		MensagemDTO mensagemRecebida = funcionarioService.adicionaNovoFuncionario(funcionarioDTO);
		MensagemDTO mensagemEsperada = new MensagemDTO(
				"O ORÇAMENTO EM FOLHA NÃO É SUFICIENTE PARA CONTRATAR ESTE FUNCIONARIO!");

		Assert.assertEquals(mensagemEsperada, mensagemRecebida);

	}

	private FuncionarioDTO criaFuncionario() {
		FuncionarioDTO funcionarioDTO = new FuncionarioDTO();

		funcionarioDTO.setNome("ELias");
		funcionarioDTO.setCpf("123.456.789-10");
		funcionarioDTO.setConcursado(true);
		funcionarioDTO.setFuncao("Desenvolvimento");
		funcionarioDTO.setDataAdmissao(LocalDate.now());
		funcionarioDTO.setSalario(8000D);
		funcionarioDTO.setIdSecretaria(11L);

		return funcionarioDTO;
	}

}
