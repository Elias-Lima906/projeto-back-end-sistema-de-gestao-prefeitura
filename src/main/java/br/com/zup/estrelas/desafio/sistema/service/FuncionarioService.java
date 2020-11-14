package br.com.zup.estrelas.desafio.sistema.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.zup.estrelas.desafio.sistema.dto.FuncionarioDTO;
import br.com.zup.estrelas.desafio.sistema.dto.MensagemDTO;
import br.com.zup.estrelas.desafio.sistema.entity.Funcionario;
import br.com.zup.estrelas.desafio.sistema.entity.Secretaria;
import br.com.zup.estrelas.desafio.sistema.repository.FuncionarioRepository;
import br.com.zup.estrelas.desafio.sistema.repository.SecretariaRepository;

@Service
public class FuncionarioService {

	@Autowired
	FuncionarioRepository funcionarioRepository;

	@Autowired
	SecretariaRepository secretariaRepository;

	public Optional<Funcionario> buscaFuncionarioPorId(Long idFuncionario) {
		if (!funcionarioRepository.existsById(idFuncionario)) {
			return Optional.empty();
		}

		return funcionarioRepository.findById(idFuncionario);
	}

	public List<Funcionario> listaFuncionarios() {

		return (List<Funcionario>) funcionarioRepository.findAll();
	}

	public MensagemDTO adicionaNovoFuncionario(FuncionarioDTO funcionarioDTO) {
		if (funcionarioRepository.existsByCpf(funcionarioDTO.getCpf())) {
			return new MensagemDTO("ESSE FUNCIONARIO JÁ ESTÁ CADASTRADO NO NOSSO SISTEMA!");
		}
		if (!secretariaRepository.existsById(funcionarioDTO.getIdSecretaria())) {
			return new MensagemDTO("A SECRETARIA EM QUESTÃO NÃO EXISTE, TENTE EXCAIXA-LO EM OUTRA SECRETARIA!");
		}

		Secretaria secretaria = secretariaRepository.findById(funcionarioDTO.getIdSecretaria()).get();

		if (secretaria.getOrcamentoFolha() < funcionarioDTO.getSalario()) {
			return new MensagemDTO("O ORÇAMENTO EM FOLHA NÃO É SUFICIENTE PARA CONTRATAR ESTE FUNCIONARIO!");
		}

		MensagemDTO criadoComSucesso = criaNovoFuncionario(funcionarioDTO, secretaria);

		alteraOrcamentoEmFolhaPosContratacao(secretaria, funcionarioDTO);

		return criadoComSucesso;
	}

	public MensagemDTO alteraInformacoesFuncionario(Long idFuncionario, FuncionarioDTO funcionarioDTO) {
		if (!funcionarioRepository.existsById(idFuncionario)) {
			return new MensagemDTO("FUNCIONARIO NÃO ENCONTRADO NO BANCO DE DADOS!");
		}
		if (!secretariaRepository.existsById(funcionarioDTO.getIdSecretaria())) {
			return new MensagemDTO("A SECRETARIA EM QUESTÃO NÃO EXISTE, TENTE EXCAIXA-LO EM OUTRA SECRETARIA!");
		}

		Funcionario antigoCadastroFuncionario = funcionarioRepository.findById(idFuncionario).get();

		if (funcionarioDTO.getSalario() < antigoCadastroFuncionario.getSalario()) {
			return new MensagemDTO("SEGUNDO AS LEIS DO CLT NÃO É POSSÍVEL REDUZIR O SALÁRIO DE UM FUNCIONARIO");
		}

		Long idAntigaSecretaria = antigoCadastroFuncionario.getIdSecretaria();
		Secretaria antigaSecretaria = secretariaRepository.findById(idAntigaSecretaria).get();
		Secretaria novaSecretaria = secretariaRepository.findById(funcionarioDTO.getIdSecretaria()).get();

		if (novaSecretaria.getOrcamentoFolha() < funcionarioDTO.getSalario()) {
			return new MensagemDTO("A NOVA SECRETARIA NÂO SUPORTA O SALARIO DESTE FUNCIONARIO");
		}

		if (antigoCadastroFuncionario.getIdSecretaria() != funcionarioDTO.getIdSecretaria()) {

			this.atualizaOrcamentoNovaEAntigaSecretaria(antigaSecretaria, novaSecretaria, funcionarioDTO,
					idFuncionario);
		}

		MensagemDTO alteradoComSucesso = this.alteraFuncionario(funcionarioDTO, novaSecretaria);

		return alteradoComSucesso;
	}

	public MensagemDTO removeFuncionario(Long idFuncionario) {
		if (!funcionarioRepository.existsById(idFuncionario)) {
			return new MensagemDTO("O FUNCIONARIO EM QUESTÃO NÃO FOI ENCONTRADO PELO ID!");
		}
		Funcionario funcionario = funcionarioRepository.findById(idFuncionario).get();
		Secretaria secretaria = secretariaRepository.findById(funcionario.getIdSecretaria()).get();

		Double orcamentoEmFolhaAtualizado = secretaria.getOrcamentoFolha() + funcionario.getSalario();

		secretaria.setOrcamentoFolha(orcamentoEmFolhaAtualizado);

		secretariaRepository.save(secretaria);
		funcionarioRepository.delete(funcionario);

		return new MensagemDTO("CADASTRO DO FUNCIONARIO REMOVIDO COM SUCESSO!");
	}

	private void alteraOrcamentoEmFolhaPosContratacao(Secretaria secretaria, FuncionarioDTO funcionarioDTO) {
		Double novoOrcamentoEmFolha = secretaria.getOrcamentoFolha() - funcionarioDTO.getSalario();
		secretaria.setOrcamentoFolha(novoOrcamentoEmFolha);

		secretariaRepository.save(secretaria);

	}

	private MensagemDTO criaNovoFuncionario(FuncionarioDTO funcionarioDTO, Secretaria secretaria) {
		Funcionario funcionario = new Funcionario();

		BeanUtils.copyProperties(funcionarioDTO, funcionario);
		funcionario.setSecretaria(secretaria);

		funcionarioRepository.save(funcionario);

		return new MensagemDTO("FUNCIONARIO DEVIDAMENTE ADICIONADO AO BANCO DE DADOS");
	}

	private void atualizaOrcamentoNovaEAntigaSecretaria(Secretaria antigaSecretaria, Secretaria novaSecretaria,
			FuncionarioDTO funcionarioDTO, Long idFuncionario) {
		Funcionario antigoCadastroFuncionario = funcionarioRepository.findById(idFuncionario).get();

		Double orcamentoAntigaSecretariaReajustado = antigaSecretaria.getOrcamentoFolha()
				+ antigoCadastroFuncionario.getSalario();
		Double orcamentoNovaSecretariaReajustado = novaSecretaria.getOrcamentoFolha() - funcionarioDTO.getSalario();

		antigaSecretaria.setOrcamentoFolha(orcamentoAntigaSecretariaReajustado);
		novaSecretaria.setOrcamentoFolha(orcamentoNovaSecretariaReajustado);

		secretariaRepository.save(antigaSecretaria);
		secretariaRepository.save(novaSecretaria);

	}

	private MensagemDTO alteraFuncionario(FuncionarioDTO funcionarioDTO, Secretaria novaSecretaria) {
		Funcionario funcionarioAlterado = funcionarioRepository.findByCpf(funcionarioDTO.getCpf());

		BeanUtils.copyProperties(funcionarioDTO, funcionarioAlterado);
		funcionarioAlterado.setSecretaria(novaSecretaria);

		funcionarioRepository.save(funcionarioAlterado);

		return new MensagemDTO("INFORMAÇÃO(ÕES) ALTERADA(S) COM SUCESSO!");
	}
}