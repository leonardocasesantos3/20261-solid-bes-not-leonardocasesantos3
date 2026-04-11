package br.com.ucsal.olimpiadas;

public class App {

	private static final Repositorio<Participante> participantes = new ListaParticipantes();
	private static final Repositorio<Prova> provas = new ListaProvas();
	private static final Repositorio<Questao> questoes = new ListaQuestoes();
	private static final Repositorio<Tentativa> tentativas = new ListaTentativas();

	public static void main(String[] args) {
		seed();
		Menu menu = new Menu(participantes, provas, questoes, tentativas);
		menu.iniciar();
	}

	static void seed() {
		var prova = new Prova();
		prova.setTitulo("Olimpíada 2026 • Nível 1 • Prova A");
		provas.adicionar(prova);

		var q1 = new QuestaoFechada();
		q1.setProvaId(prova.getId());

		q1.setEnunciado("""
				Questão 1 — Mate em 1.
				É a vez das brancas.
				Encontre o lance que dá mate imediatamente.
				""");

		q1.setFenInicial("6k1/5ppp/8/8/8/7Q/6PP/6K1 w - - 0 1");

		q1.setAlternativas(new String[] { "A) Qh7#", "B) Qf5#", "C) Qc8#", "D) Qh8#", "E) Qe6#" });

		q1.setAlternativaCorreta('C');

		questoes.adicionar(q1);
	}
}