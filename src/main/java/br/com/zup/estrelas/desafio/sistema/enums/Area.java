package br.com.zup.estrelas.desafio.sistema.enums;

public enum Area {

	SAUDE("Saúde"), SEGURANCA("Segurança"), ESPORTE("Esporte"), EDUCACAO("Educação"), CULTURA("Cultura"),
	TRANSPORTE("Transporte");

	private String value;

	Area(String valor) {
		this.value = valor;
	}

	public String getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return this.value;
	}

}
