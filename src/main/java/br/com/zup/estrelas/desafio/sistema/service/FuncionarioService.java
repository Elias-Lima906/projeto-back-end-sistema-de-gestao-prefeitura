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

		Optional<Funcionario> antigoCadastroFuncionario = funcionarioRepository.findById(idFuncionario);
		Optional<Secretaria> secretaria = secretariaRepository
				.findById(antigoCadastroFuncionario.get().getIdSecretaria());

		if (!antigoCadastroFuncionario.isPresent()) {
			return new MensagemDTO("FUNCIONARIO NÃO ENCONTRADO NO BANCO DE DADOS!");
		}

		if (funcionarioDTO.getSalario() < antigoCadastroFuncionario.get().getSalario()) {
			return new MensagemDTO("SEGUNDO AS LEIS DO CLT NÃO É POSSÍVEL REDUZIR O SALÁRIO DE UM FUNCIONARIO");
		}

		if (secretaria.get().getOrcamentoFolha() < funcionarioDTO.getSalario()) {
			return new MensagemDTO("A SECRETARIA ATUAL NÂO SUPORTA O SALARIO DESTE FUNCIONARIO");
		}

		Optional<Secretaria> novaSecretaria = secretariaRepository.findById(funcionarioDTO.getIdSecretaria());

		if (antigoCadastroFuncionario.get().getIdSecretaria() != funcionarioDTO.getIdSecretaria()) {

			if (!novaSecretaria.isPresent()) {
				return new MensagemDTO("A SECRETARIA EM QUESTÃO NÃO EXISTE, TENTE EXCAIXA-LO EM OUTRA SECRETARIA!");
			}

			if (novaSecretaria.get().getOrcamentoFolha() < funcionarioDTO.getSalario()) {
				return new MensagemDTO("A NOVA SECRETARIA NÂO SUPORTA O SALARIO DESTE FUNCIONARIO");
			}

			this.atualizaOrcamentoNovaEAntigaSecretaria(secretaria, novaSecretaria, funcionarioDTO, idFuncionario);
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

	private void atualizaOrcamentoNovaEAntigaSecretaria(Optional<Secretaria> antigaSecretaria,
			Optional<Secretaria> novaSecretaria, FuncionarioDTO funcionarioDTO, Long idFuncionario) {
		Funcionario antigoCadastroFuncionario = funcionarioRepository.findById(idFuncionario).get();

		Double orcamentoAntigaSecretariaReajustado = antigaSecretaria.get().getOrcamentoFolha()
				+ antigoCadastroFuncionario.getSalario();
		Double orcamentoNovaSecretariaReajustado = novaSecretaria.get().getOrcamentoFolha()
				- funcionarioDTO.getSalario();

		antigaSecretaria.get().setOrcamentoFolha(orcamentoAntigaSecretariaReajustado);
		novaSecretaria.get().setOrcamentoFolha(orcamentoNovaSecretariaReajustado);

		secretariaRepository.save(antigaSecretaria.get());
		secretariaRepository.save(novaSecretaria.get());
	}

	private MensagemDTO alteraFuncionario(FuncionarioDTO funcionarioDTO, Optional<Secretaria> novaSecretaria) {
		Funcionario funcionarioAlterado = funcionarioRepository.findByCpf(funcionarioDTO.getCpf());

		BeanUtils.copyProperties(funcionarioDTO, funcionarioAlterado);
		funcionarioAlterado.setSecretaria(novaSecretaria.get());

		funcionarioRepository.save(funcionarioAlterado);

		return new MensagemDTO("INFORMAÇÃO(ÕES) ALTERADA(S) COM SUCESSO!");
	}
}