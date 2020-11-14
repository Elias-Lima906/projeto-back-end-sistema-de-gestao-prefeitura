package br.com.zup.estrelas.desafio.sistema.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.zup.estrelas.desafio.sistema.dto.MensagemDTO;
import br.com.zup.estrelas.desafio.sistema.dto.SecretariaDTO;
import br.com.zup.estrelas.desafio.sistema.entity.Secretaria;
import br.com.zup.estrelas.desafio.sistema.repository.SecretariaRepository;

@Service
public class SecretariaService {

	@Autowired
	SecretariaRepository secretariaRepository;

	public Optional<Secretaria> buscaSecretariaPorId(Long idSecretaria) {
		if (!secretariaRepository.existsById(idSecretaria)) {
			return Optional.empty();
		}
		return secretariaRepository.findById(idSecretaria);
	}

	public List<Secretaria> listaTodasAsSecretarias() {
		return (List<Secretaria>) secretariaRepository.findAll();
	}

	public MensagemDTO adicionaSecretaria(SecretariaDTO secretariaDTO) {
		if (secretariaRepository.existsByArea(secretariaDTO.getArea())) {
			return new MensagemDTO("JÁ EXISTE UMA SECRETARIA PARA ESTÁ ÁREA NA PREFEITURA!");
		}

		MensagemDTO criadoComSucesso = criaSecretaria(secretariaDTO);

		return criadoComSucesso;

	}

	public MensagemDTO alteraSecretaria(Long idSecretaria, SecretariaDTO secretariaDTO) {
		if (!secretariaRepository.existsById(idSecretaria)) {
			return new MensagemDTO("A SECRETARIA EM QEUSTÃO NÃO EXISTE!");
		}

		Secretaria secretaria = secretariaRepository.findById(idSecretaria).get();
		
		if (secretaria.getArea() != secretariaDTO.getArea()
				&& secretariaRepository.existsByArea(secretariaDTO.getArea())) {
			return new MensagemDTO("JÁ EXISTE UMA SECRETARIA PARA ESTÁ ÁREA NA PREFEITURA!");
		}

		MensagemDTO alteradoComSucesso = alteraInformacaoSecretaria(idSecretaria, secretariaDTO);

		return alteradoComSucesso;
	}

	public MensagemDTO removeSecretaria(Long idSecretaria) {
		if (!secretariaRepository.existsById(idSecretaria)) {
			return new MensagemDTO("ESSA SECRETARIA AINDA NÃO FOI CRIADA!");
		}

		Optional<Secretaria> secretaria = secretariaRepository.findById(idSecretaria);
		secretariaRepository.delete(secretaria.get());
		return new MensagemDTO("SECRETARIA DESCONTINUADA COM SUCESSO!");
	}

	private MensagemDTO criaSecretaria(SecretariaDTO secretariaDTO) {
		Secretaria secretaria = new Secretaria();

		BeanUtils.copyProperties(secretariaDTO, secretaria);
		secretaria.setFuncionarios(Collections.emptyList());
		secretaria.setProjetos(Collections.emptyList());

		secretariaRepository.save(secretaria);

		return new MensagemDTO("SECRETARIA DE(A) " + secretaria.getArea() + " CRIADA COM SUCESSO!");
	}

	private MensagemDTO alteraInformacaoSecretaria(Long idSecretaria, SecretariaDTO secretariaDTO) {
		Secretaria secretaria = secretariaRepository.findById(idSecretaria).get();

		BeanUtils.copyProperties(secretariaDTO, secretaria);

		secretariaRepository.save(secretaria);

		return new MensagemDTO("ALTERAÇÃO APROVADA, E EFETUADA NA SECRETARIA!");
	}
}
