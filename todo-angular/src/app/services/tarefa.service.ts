import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Tarefa, StatusTarefa } from '../models/tarefa.model';

/**
 * Service atualizado para consumir a API REST do Spring Boot.
 * O HttpClient faz as requisições HTTP para o backend.
 */
@Injectable({
  providedIn: 'root'
})
export class TarefaService {

  // URL base da API — deve bater com o backend Spring Boot
  private readonly API = 'http://localhost:8088/api/tarefas';

  constructor(private http: HttpClient) {}

  // GET /api/tarefas
  listarTodas(): Observable<Tarefa[]> {
    return this.http.get<Tarefa[]>(this.API);
  }

  // GET /api/tarefas?status=PENDENTE
  listarPorStatus(status: string): Observable<Tarefa[]> {
    return this.http.get<Tarefa[]>(`${this.API}?status=${status}`);
  }

  // POST /api/tarefas
  criar(titulo: string): Observable<Tarefa> {
    return this.http.post<Tarefa>(this.API, { titulo });
  }

  // PATCH /api/tarefas/{id}/status
  atualizarStatus(id: number, status: StatusTarefa): Observable<Tarefa> {
    // Converte o status do Angular para o formato do backend (ex: em_execucao → EM_EXECUCAO)
    const statusBackend = status.toUpperCase();
    return this.http.patch<Tarefa>(`${this.API}/${id}/status`, { status: statusBackend });
  }

  // PUT /api/tarefas/{id}
  editarTitulo(id: number, titulo: string): Observable<Tarefa> {
    return this.http.put<Tarefa>(`${this.API}/${id}`, { titulo });
  }

  // DELETE /api/tarefas/{id}
  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API}/${id}`);
  }
}
