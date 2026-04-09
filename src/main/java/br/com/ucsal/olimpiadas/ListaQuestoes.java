package br.com.ucsal.olimpiadas;

import java.util.ArrayList;
import java.util.List;

public class ListaQuestoes implements Armazenamento<Questao> {
    private long proximoId = 1;
    private final List<Questao> questoes = new ArrayList<>();

    @Override
    public void adicionar(Questao q) {
        if (q.getId() == 0) {
            q.setId(proximoId++);
        }
        questoes.add(q);
    }

    @Override
    public List<Questao> listarTodos() {
        return questoes;
    }

    @Override
    public Questao buscarPorId(long id) {
        for (Questao q : questoes) {
            if (q.getId() == id) {
                return q;
            }
        }
        return null;
    }
}
