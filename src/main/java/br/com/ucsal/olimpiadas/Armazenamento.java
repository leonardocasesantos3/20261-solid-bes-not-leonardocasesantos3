package br.com.ucsal.olimpiadas;

import java.util.List;

public interface Armazenamento<T> {
    void adicionar(T entidade);
    List<T> listarTodos();
    T buscarPorId(long id);
}
