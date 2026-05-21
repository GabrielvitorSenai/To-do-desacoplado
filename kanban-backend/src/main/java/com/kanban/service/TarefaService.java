package com.kanban.service;

import com.kanban.dto.TarefaDTO;
import com.kanban.exception.TarefaNaoEncontradaException;
import com.kanban.model.StatusTarefa;
import com.kanban.model.Tarefa;
import com.kanban.repository.TarefaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Camada de serviço — contém a lógica de negócio.
 *
 * O @Service registra esta classe no contexto do Spring,
 * permitindo que seja injetada em outros componentes.
 *
 * O @Transactional garante que operações de escrita sejam
 * atômicas — ou tudo é salvo, ou nada é.
 */
@Service
@Transactional
public class TarefaService {

    private final TarefaRepository repository;

    // Injeção de dependência via construtor (prática recomendada)
    public TarefaService(TarefaRepository repository) {
        this.repository = repository;
    }

    // ── Listar ────────────────────────────────────────────────

    /** Retorna todas as tarefas ordenadas da mais recente para a mais antiga */
    @Transactional(readOnly = true)
    public List<TarefaDTO.Response> listarTodas() {
        return repository.findAllByOrderByDataCriacaoDesc()
                .stream()
                .map(TarefaDTO.Response::from)
                .toList();
    }

    /** Retorna apenas as tarefas de um determinado status */
    @Transactional(readOnly = true)
    public List<TarefaDTO.Response> listarPorStatus(StatusTarefa status) {
        return repository.findByStatusOrderByDataCriacaoDesc(status)
                .stream()
                .map(TarefaDTO.Response::from)
                .toList();
    }

    /** Busca uma tarefa específica pelo ID */
    @Transactional(readOnly = true)
    public TarefaDTO.Response buscarPorId(Long id) {
        Tarefa tarefa = buscarOuLancarErro(id);
        return TarefaDTO.Response.from(tarefa);
    }

    // ── Criar ─────────────────────────────────────────────────

    /** Cria uma nova tarefa com status PENDENTE */
    public TarefaDTO.Response criar(TarefaDTO.CriarRequest request) {
        Tarefa tarefa = new Tarefa(request.getTitulo());
        Tarefa salva = repository.save(tarefa);
        return TarefaDTO.Response.from(salva);
    }

    // ── Atualizar ─────────────────────────────────────────────

    /** Edita o título de uma tarefa existente */
    public TarefaDTO.Response editarTitulo(Long id, TarefaDTO.EditarRequest request) {
        Tarefa tarefa = buscarOuLancarErro(id);
        tarefa.setTitulo(request.getTitulo());
        return TarefaDTO.Response.from(repository.save(tarefa));
    }

    /** Atualiza o status de uma tarefa (move entre colunas do Kanban) */
    public TarefaDTO.Response atualizarStatus(Long id, TarefaDTO.AtualizarStatusRequest request) {
        Tarefa tarefa = buscarOuLancarErro(id);
        tarefa.setStatus(request.getStatus());
        return TarefaDTO.Response.from(repository.save(tarefa));
    }

    // ── Excluir ───────────────────────────────────────────────

    /** Remove uma tarefa permanentemente do banco */
    public void excluir(Long id) {
        Tarefa tarefa = buscarOuLancarErro(id);
        repository.delete(tarefa);
    }

    // ── Utilitário privado ────────────────────────────────────

    /** Busca a tarefa ou lança exceção com mensagem clara */
    private Tarefa buscarOuLancarErro(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException(id));
    }
}
