package com.kanban.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Entidade que representa uma tarefa no banco de dados.
 * A anotação @Entity diz ao JPA que esta classe corresponde a uma tabela.
 */
@Entity
@Table(name = "tarefas")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título não pode ser vazio")
    @Size(max = 255, message = "Título deve ter no máximo 255 caracteres")
    @Column(nullable = false)
    private String titulo;

    /**
     * Status da tarefa — corresponde às colunas do Kanban.
     * Usamos @Enumerated(EnumType.STRING) para salvar o texto
     * no banco (ex: "PENDENTE") em vez de um número inteiro.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTarefa status;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    // ── Lifecycle Hooks do JPA ────────────────────────────────

    /** Executado automaticamente antes de inserir no banco */
    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
        if (this.status == null) {
            this.status = StatusTarefa.PENDENTE;
        }
    }

    /** Executado automaticamente antes de atualizar no banco */
    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    // ── Construtores ──────────────────────────────────────────

    public Tarefa() {}

    public Tarefa(String titulo) {
        this.titulo = titulo;
        this.status = StatusTarefa.PENDENTE;
    }

    // ── Getters e Setters ─────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public StatusTarefa getStatus() { return status; }
    public void setStatus(StatusTarefa status) { this.status = status; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}
