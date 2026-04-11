package br.com.ucsal.olimpiadas;

import java.util.Scanner;

public class Menu {

	private final Repositorio<Participante> participantes;
	private final Repositorio<Prova> provas;
	private final Repositorio<Questao> questoes;
	private final Repositorio<Tentativa> tentativas;

	private final Scanner in = new Scanner(System.in);

	public Menu(Repositorio<Participante> participantes, Repositorio<Prova> provas, Repositorio<Questao> questoes,
			Repositorio<Tentativa> tentativas) {
		this.participantes = participantes;
		this.provas = provas;
		this.questoes = questoes;
		this.tentativas = tentativas;
	}

	public void iniciar() {
		while (true) {
			System.out.println("\n=== OLIMPÍADA DE QUESTÕES ===");
			System.out.println("1) Cadastrar participante");
			System.out.println("2) Cadastrar prova");
			System.out.println("3) Cadastrar questão em uma prova");
			System.out.println("4) Aplicar prova (selecionar participante + prova)");
			System.out.println("5) Listar tentativas (resumo)");
			System.out.println("0) Sair");
			System.out.print("> ");

			switch (in.nextLine()) {
			case "1" -> cadastrarParticipante();
			case "2" -> cadastrarProva();
			case "3" -> cadastrarQuestao();
			case "4" -> aplicarProva();
			case "5" -> listarTentativas();
			case "0" -> {
				System.out.println("tchau");
				return;
			}
			default -> System.out.println("opção inválida");
			}
		}
	}

	private void cadastrarParticipante() {
		System.out.print("Nome: ");
		var nome = in.nextLine();

		System.out.print("Email (opcional): ");
		var email = in.nextLine();

		if (nome == null || nome.isBlank()) {
			System.out.println("nome inválido");
			return;
		}

		var p = new Participante();
		p.setNome(nome);
		p.setEmail(email);

		participantes.adicionar(p);
		System.out.println("Participante cadastrado: " + p.getId());
	}

	private void cadastrarProva() {
		System.out.print("Título da prova: ");
		var titulo = in.nextLine();

		if (titulo == null || titulo.isBlank()) {
			System.out.println("título inválido");
			return;
		}

		var prova = new Prova();
		prova.setTitulo(titulo);

		provas.adicionar(prova);
		System.out.println("Prova criada: " + prova.getId());
	}

	private void cadastrarQuestao() {
		if (provas.listarTodos().isEmpty()) {
			System.out.println("não há provas cadastradas");
			return;
		}

		var provaId = escolherProva();
		if (provaId == null)
			return;

		System.out.println("Enunciado:");
		var enunciado = in.nextLine();

		var alternativas = new String[5];
		for (int i = 0; i < 5; i++) {
			char letra = (char) ('A' + i);
			System.out.print("Alternativa " + letra + ": ");
			alternativas[i] = letra + ") " + in.nextLine();
		}

		System.out.print("Alternativa correta (A–E): ");
		char correta;
		try {
			correta = QuestaoFechada.normalizar(in.nextLine().trim().charAt(0));
		} catch (Exception e) {
			System.out.println("alternativa inválida");
			return;
		}

		var q = new QuestaoFechada();
		q.setProvaId(provaId);
		q.setEnunciado(enunciado);
		q.setAlternativas(alternativas);
		q.setAlternativaCorreta(correta);

		questoes.adicionar(q);

		System.out.println("Questão cadastrada: " + q.getId() + " (na prova " + provaId + ")");
	}

	private void aplicarProva() {
		if (participantes.listarTodos().isEmpty()) {
			System.out.println("cadastre participantes primeiro");
			return;
		}
		if (provas.listarTodos().isEmpty()) {
			System.out.println("cadastre provas primeiro");
			return;
		}

		var participanteId = escolherParticipante();
		if (participanteId == null)
			return;

		var provaId = escolherProva();
		if (provaId == null)
			return;

		var questoesDaProva = questoes.listarTodos().stream().filter(q -> q.getProvaId() == provaId).toList();

		if (questoesDaProva.isEmpty()) {
			System.out.println("esta prova não possui questões cadastradas");
			return;
		}

		var tentativa = new Tentativa();
		tentativa.setParticipanteId(participanteId);
		tentativa.setProvaId(provaId);

		System.out.println("\n--- Início da Prova ---");

		for (var q : questoesDaProva) {
			System.out.println("\nQuestão #" + q.getId());
			System.out.println(q.getEnunciado());

			if (q.getFenInicial() != null && !q.getFenInicial().isBlank()) {
				System.out.println("Posição inicial:");
				Tabuleiro.imprimirFen(q.getFenInicial());
			}

			if (q instanceof QuestaoFechada qf) {
				for (var alt : qf.getAlternativas()) {
					System.out.println(alt);
				}
			}

			System.out.print("Sua resposta: ");
			String informada = in.nextLine().trim();

			var r = new Resposta();
			r.setQuestaoId(q.getId());
			r.setValorInformado(informada);
			r.setCorreta(q.isRespostaCorreta(informada));

			tentativa.getRespostas().add(r);
		}

		tentativas.adicionar(tentativa);

		int nota = tentativa.calcularNota();
		System.out.println("\n--- Fim da Prova ---");
		System.out.println("Nota (acertos): " + nota + " / " + tentativa.getRespostas().size());
	}

	private void listarTentativas() {
		System.out.println("\n--- Tentativas ---");
		for (var t : tentativas.listarTodos()) {
			System.out.printf("#%d | participante=%d | prova=%d | nota=%d/%d%n", t.getId(), t.getParticipanteId(),
					t.getProvaId(), t.calcularNota(), t.getRespostas().size());
		}
	}

	private Long escolherParticipante() {
		System.out.println("\nParticipantes:");
		for (var p : participantes.listarTodos()) {
			System.out.printf("  %d) %s%n", p.getId(), p.getNome());
		}
		System.out.print("Escolha o id do participante: ");

		try {
			long id = Long.parseLong(in.nextLine());
			boolean existe = participantes.buscarPorId(id) != null;
			if (!existe) {
				System.out.println("id inválido");
				return null;
			}
			return id;
		} catch (Exception e) {
			System.out.println("entrada inválida");
			return null;
		}
	}

	private Long escolherProva() {
		System.out.println("\nProvas:");
		for (var p : provas.listarTodos()) {
			System.out.printf("  %d) %s%n", p.getId(), p.getTitulo());
		}
		System.out.print("Escolha o id da prova: ");

		try {
			long id = Long.parseLong(in.nextLine());
			boolean existe = provas.buscarPorId(id) != null;
			if (!existe) {
				System.out.println("id inválido");
				return null;
			}
			return id;
		} catch (Exception e) {
			System.out.println("entrada inválida");
			return null;
		}
	}
}
