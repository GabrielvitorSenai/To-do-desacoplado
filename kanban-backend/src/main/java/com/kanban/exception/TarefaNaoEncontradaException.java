package com.kanban.exception;

/**
 * Exceção lançada quando uma tarefa não é encontrada pelo ID.
 * Estende RuntimeException para não precisar ser declarada
 * nos métodos que a lançam.
 */
public class TarefaNaoEncontradaException extends RuntimeException {

    public TarefaNaoEncontradaException(Long id) {
        super("Tarefa não encontrada com o ID: " + id);
    }
}
