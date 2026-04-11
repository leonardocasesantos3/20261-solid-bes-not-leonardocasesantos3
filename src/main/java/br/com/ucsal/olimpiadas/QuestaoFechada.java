package br.com.ucsal.olimpiadas;

import java.util.Arrays;

public class QuestaoFechada extends Questao {

	private String[] alternativas = new String[5];
	private char alternativaCorreta;

	public String[] getAlternativas() {
		return alternativas;
	}

	public void setAlternativas(String[] alternativas) {
		if (alternativas == null || alternativas.length != 5) {
			throw new IllegalArgumentException("A questão deve possuir exatamente 5 alternativas.");
		}
		this.alternativas = Arrays.copyOf(alternativas, 5);
	}

	public char getAlternativaCorreta() {
		return alternativaCorreta;
	}

	public void setAlternativaCorreta(char alternativaCorreta) {
		this.alternativaCorreta = normalizar(alternativaCorreta);
	}

	@Override
	public boolean isRespostaCorreta(String resposta) {
		if (resposta == null || resposta.trim().isEmpty()) return false;
		try {
			return normalizar(resposta.trim().charAt(0)) == alternativaCorreta;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public static char normalizar(char c) {
		char up = Character.toUpperCase(c);
		if (up < 'A' || up > 'E') {
			throw new IllegalArgumentException("Alternativa deve estar entre A e E.");
		}
		return up;
	}
}
