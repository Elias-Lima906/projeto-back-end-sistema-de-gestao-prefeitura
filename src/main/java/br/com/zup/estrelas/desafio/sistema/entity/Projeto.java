package br.com.zup.estrelas.desafio.sistema.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Projeto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_projeto")
	Long idProjeto;

	@Column(nullable = false)
	String nome;

	@Column(nullable = false)
	String descricao;

	@Column(nullable = false)
	Double custo;

	@ManyToOne
	@JoinColumn(name = "id_secretaria", foreignKey = @ForeignKey(name = "projetos_fk"))
	private Secretaria secretaria;

	@Column(name = "data_inicio", nullable = false)
	LocalDate dataInicio;

	@Column(name = "data_entrega")
	LocalDate dataEntrega;

	@Column(nullable = false)
	Boolean concluido;

	public Long getIdProjeto() {
		return idProjeto;
	}

	public void setIdProjeto(Long idProjeto) {
		this.idProjeto = idProjeto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getCusto() {
		return custo;
	}

	public void setCusto(Double custo) {
		this.custo = custo;
	}

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public Long getIdSecretaria() {
		return secretaria.getIdSecretaria();
	}

	public void setSecretaria(Secretaria secretaria) {
		this.secretaria = secretaria;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDate getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(LocalDate dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public Boolean getConcluido() {
		return concluido;
	}

	public void setConcluido(Boolean concluido) {
		this.concluido = concluido;
	}

}
