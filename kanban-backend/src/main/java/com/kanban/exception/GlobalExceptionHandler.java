package com.kanban.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Tratador global de exceções.
 *
 * O @RestControllerAdvice intercepta exceções lançadas em qualquer
 * Controller e retorna respostas HTTP padronizadas em JSON.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Trata tarefa não encontrada → HTTP 404 */
    @ExceptionHandler(TarefaNaoEncontradaException.class)
    public ResponseEntity<Map<String, Object>> handleNaoEncontrada(TarefaNaoEncontradaException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /** Trata erros de validação (@NotBlank, @Size etc.) → HTTP 400 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidacao(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Dados inválidos");
        return buildError(HttpStatus.BAD_REQUEST, mensagem);
    }

    /** Trata qualquer outra exceção inesperada → HTTP 500 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeral(Exception ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor: " + ex.getMessage());
    }

    // ── Utilitário ────────────────────────────────────────────

    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String mensagem) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("erro", status.getReasonPhrase());
        body.put("mensagem", mensagem);
        return ResponseEntity.status(status).body(body);
    }
}
