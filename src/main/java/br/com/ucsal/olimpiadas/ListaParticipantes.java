package br.com.ucsal.olimpiadas;

import java.util.ArrayList;
import java.util.List;

public class ListaParticipantes implements Repositorio<Participante> {
    private long proximoId = 1;
    private final List<Participante> participantes = new ArrayList<>();

    @Override
    public void adicionar(Participante p) {
        if (p.getId() == 0) {
            p.setId(proximoId++);
        }
        participantes.add(p);
    }

    @Override
    public List<Participante> listarTodos() {
        return participantes;
    }

    @Override
    public Participante buscarPorId(long id) {
        for (Participante p : participantes) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
}
