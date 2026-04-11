package br.com.ucsal.olimpiadas;

public class Resposta {

	private long questaoId;
	private String valorInformado;
	private boolean correta;

	public long getQuestaoId() {
		return questaoId;
	}

	public void setQuestaoId(long questaoId) {
		this.questaoId = questaoId;
	}

	public String getValorInformado() {
		return valorInformado;
	}

	public void setValorInformado(String valorInformado) {
		this.valorInformado = valorInformado;
	}

	public boolean isCorreta() {
		return correta;
	}

	public void setCorreta(boolean correta) {
		this.correta = correta;
	}

}
