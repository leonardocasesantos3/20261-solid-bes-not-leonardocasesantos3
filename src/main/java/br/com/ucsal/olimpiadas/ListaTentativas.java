package br.com.ucsal.olimpiadas;

import java.util.ArrayList;
import java.util.List;

public class ListaTentativas implements Repositorio<Tentativa> {
    private long proximoId = 1;
    private final List<Tentativa> tentativas = new ArrayList<>();

    @Override
    public void adicionar(Tentativa t) {
        if (t.getId() == 0) {
            t.setId(proximoId++);
        }
        tentativas.add(t);
    }

    @Override
    public List<Tentativa> listarTodos() {
        return tentativas;
    }

    @Override
    public Tentativa buscarPorId(long id) {
        for (Tentativa t : tentativas) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }
}
