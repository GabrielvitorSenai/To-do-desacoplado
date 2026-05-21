package com.kanban.repository;

import com.kanban.model.StatusTarefa;
import com.kanban.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository da Tarefa.
 *
 * O Spring Data JPA gera a implementação automaticamente em tempo de execução.
 * Só precisamos declarar a interface — o framework cuida do resto.
 *
 * JpaRepository<Tarefa, Long> já fornece:
 *   - findAll()
 *   - findById(id)
 *   - save(tarefa)
 *   - deleteById(id)
 *   - count(), existsById(), etc.
 */
@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    /**
     * Busca tarefas por status.
     * O Spring Data interpreta o nome do método e gera o SQL:
     * SELECT * FROM tarefas WHERE status = ?
     */
    List<Tarefa> findByStatusOrderByDataCriacaoDesc(StatusTarefa status);

    /**
     * Busca todas as tarefas ordenadas pela data de criação (mais recentes primeiro)
     */
    List<Tarefa> findAllByOrderByDataCriacaoDesc();
}
