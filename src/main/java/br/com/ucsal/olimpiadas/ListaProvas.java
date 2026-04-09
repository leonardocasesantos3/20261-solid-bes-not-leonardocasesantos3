package br.com.ucsal.olimpiadas;

import java.util.ArrayList;
import java.util.List;

public class ListaProvas implements Armazenamento<Prova> {
    private long proximoId = 1;
    private final List<Prova> provas = new ArrayList<>();

    @Override
    public void adicionar(Prova p) {
        if (p.getId() == 0) {
            p.setId(proximoId++);
        }
        provas.add(p);
    }

    @Override
    public List<Prova> listarTodos() {
        return provas;
    }

    @Override
    public Prova buscarPorId(long id) {
        for (Prova p : provas) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
}
