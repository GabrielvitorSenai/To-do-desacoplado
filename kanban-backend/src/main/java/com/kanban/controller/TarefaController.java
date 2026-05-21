package com.kanban.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kanban.dto.TarefaDTO;
import com.kanban.model.StatusTarefa;
import com.kanban.service.TarefaService;

import jakarta.validation.Valid;

/**
 * Controller REST — expõe os endpoints da API.
 *
 * @RestController = @Controller + @ResponseBody (retorna JSON automaticamente)
 * @RequestMapping define o prefixo base de todas as rotas: /api/tarefas
 * @CrossOrigin permite que o Angular (localhost:4200) acesse a API
 */
@RestController
@RequestMapping("/api/tarefas")
@CrossOrigin(origins = "http://localhost:4200\", \"http://10.74.241.111:4200")
public class TarefaController {

    private final TarefaService service;

    public TarefaController(TarefaService service) {
        this.service = service;
    }

    // ── GET /api/tarefas ──────────────────────────────────────
    // Retorna todas as tarefas (ou filtra por status via query param)
    // Exemplos:
    //   GET /api/tarefas             → todas
    //   GET /api/tarefas?status=PENDENTE
    //   GET /api/tarefas?status=EM_EXECUCAO
    //   GET /api/tarefas?status=CONCLUIDA
    @GetMapping
    public ResponseEntity<List<TarefaDTO.Response>> listar(
            @RequestParam(required = false) StatusTarefa status) {

        List<TarefaDTO.Response> tarefas = (status != null)
                ? service.listarPorStatus(status)
                : service.listarTodas();

        return ResponseEntity.ok(tarefas);
    }

    // ── GET /api/tarefas/{id} ─────────────────────────────────
    // Retorna uma tarefa específica
    @GetMapping("/{id}")
    public ResponseEntity<TarefaDTO.Response> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // ── POST /api/tarefas ─────────────────────────────────────
    // Cria uma nova tarefa — retorna HTTP 201 Created
    // Body esperado: { "titulo": "Minha tarefa" }
    @PostMapping
    public ResponseEntity<TarefaDTO.Response> criar(
            @RequestBody @Valid TarefaDTO.CriarRequest request) {

        TarefaDTO.Response criada = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    // ── PUT /api/tarefas/{id} ─────────────────────────────────
    // Edita o título de uma tarefa
    // Body esperado: { "titulo": "Novo título" }
    @PutMapping("/{id}")
    public ResponseEntity<TarefaDTO.Response> editar(
            @PathVariable Long id,
            @RequestBody @Valid TarefaDTO.EditarRequest request) {

        return ResponseEntity.ok(service.editarTitulo(id, request));
    }

    // ── PATCH /api/tarefas/{id}/status ───────────────────────
    // Atualiza apenas o status (move o card entre colunas)
    // Body esperado: { "status": "EM_EXECUCAO" }
    @PatchMapping("/{id}/status")
    public ResponseEntity<TarefaDTO.Response> atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid TarefaDTO.AtualizarStatusRequest request) {

        return ResponseEntity.ok(service.atualizarStatus(id, request));
    }

    // ── DELETE /api/tarefas/{id} ──────────────────────────────
    // Remove a tarefa — retorna HTTP 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
