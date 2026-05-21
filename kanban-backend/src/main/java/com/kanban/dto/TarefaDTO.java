package com.kanban.dto;

import com.kanban.model.StatusTarefa;
import com.kanban.model.Tarefa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTOs (Data Transfer Objects) — objetos que trafegam entre
 * o frontend e o backend pela API REST.
 *
 * Separar DTOs da entidade é uma boa prática: evita expor
 * campos internos do banco e permite validar os dados de entrada.
 */
public class TarefaDTO {

    // ── DTO de Requisição (o que o Angular envia) ─────────────

    /** Usado no POST /api/tarefas para criar uma nova tarefa */
    public static class CriarRequest {

        @NotBlank(message = "O título é obrigatório")
        @Size(max = 255, message = "Título deve ter no máximo 255 caracteres")
        private String titulo;

        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }
    }

    /** Usado no PATCH /api/tarefas/{id}/status para atualizar o status */
    public static class AtualizarStatusRequest {

        @NotNull(message = "O status é obrigatório")
        private StatusTarefa status;

        public StatusTarefa getStatus() { return status; }
        public void setStatus(StatusTarefa status) { this.status = status; }
    }

    /** Usado no PUT /api/tarefas/{id} para editar o título */
    public static class EditarRequest {

        @NotBlank(message = "O título é obrigatório")
        @Size(max = 255, message = "Título deve ter no máximo 255 caracteres")
        private String titulo;

        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }
    }

    // ── DTO de Resposta (o que a API devolve ao Angular) ──────

    /** Representa uma tarefa na resposta JSON */
    public static class Response {
        private Long id;
        private String titulo;
        private StatusTarefa status;
        private LocalDateTime dataCriacao;
        private LocalDateTime dataAtualizacao;

        /** Converte uma entidade Tarefa em Response DTO */
        public static Response from(Tarefa tarefa) {
            Response dto = new Response();
            dto.id              = tarefa.getId();
            dto.titulo          = tarefa.getTitulo();
            dto.status          = tarefa.getStatus();
            dto.dataCriacao     = tarefa.getDataCriacao();
            dto.dataAtualizacao = tarefa.getDataAtualizacao();
            return dto;
        }

        public Long getId()                        { return id; }
        public String getTitulo()                  { return titulo; }
        public StatusTarefa getStatus()            { return status; }
        public LocalDateTime getDataCriacao()      { return dataCriacao; }
        public LocalDateTime getDataAtualizacao()  { return dataAtualizacao; }
    }
}
