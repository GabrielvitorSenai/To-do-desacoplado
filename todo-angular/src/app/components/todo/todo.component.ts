import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DragDropModule, CdkDragDrop, transferArrayItem } from '@angular/cdk/drag-drop';
import { TarefaService } from '../../services/tarefa.service';
import { Tarefa, StatusTarefa } from '../../models/tarefa.model';

@Component({
  selector: 'app-todo',
  standalone: true,
  imports: [CommonModule, FormsModule, DragDropModule],
  templateUrl: './todo.component.html',
  styleUrls: ['./todo.component.css']
})
export class TodoComponent implements OnInit {

  novoTitulo: string = '';
  pendentes:  Tarefa[] = [];
  emExecucao: Tarefa[] = [];
  concluidas: Tarefa[] = [];
  modoEscuro: boolean = false;
  carregando: boolean = false;
  erro: string = '';

  private readonly CHAVE_TEMA = 'kanban_tema';

  constructor(private service: TarefaService) {}

  ngOnInit(): void {
    this.modoEscuro = localStorage.getItem(this.CHAVE_TEMA) === 'escuro';
    this.aplicarTema();
    this.carregarTarefas();
  }

  carregarTarefas(): void {
    this.carregando = true;
    this.erro = '';
    this.service.listarTodas().subscribe({
      next: (tarefas) => {
        this.pendentes  = tarefas.filter((t: any) => t.status === 'PENDENTE');
        this.emExecucao = tarefas.filter((t: any) => t.status === 'EM_EXECUCAO');
        this.concluidas = tarefas.filter((t: any) => t.status === 'CONCLUIDA');
        this.carregando = false;
      },
      error: () => {
        this.erro = 'Não foi possível conectar ao servidor. Verifique se o backend está rodando.';
        this.carregando = false;
      }
    });
  }

  criar(): void {
    if (!this.novoTitulo.trim()) return;
    this.service.criar(this.novoTitulo).subscribe({
      next: () => { this.novoTitulo = ''; this.carregarTarefas(); },
      error: () => { this.erro = 'Erro ao criar tarefa.'; }
    });
  }

  excluir(id: number): void {
    this.service.excluir(id).subscribe({
      next: () => this.carregarTarefas(),
      error: () => { this.erro = 'Erro ao excluir tarefa.'; }
    });
  }

  // ── Botões de avançar/retroceder ─────────────────────────────
  // Lógica simples: chama a API diretamente com o novo status

  iniciar(tarefa: Tarefa): void {
    this.moverTarefa(tarefa, 'EM_EXECUCAO');
  }

  concluir(tarefa: Tarefa): void {
    this.moverTarefa(tarefa, 'CONCLUIDA');
  }

  reabrir(tarefa: Tarefa): void {
    this.moverTarefa(tarefa, 'EM_EXECUCAO');
  }

  private moverTarefa(tarefa: Tarefa, novoStatus: string): void {
    this.service.atualizarStatus(tarefa.id, novoStatus as StatusTarefa).subscribe({
      next: () => this.carregarTarefas(),
      error: () => { this.erro = 'Erro ao mover tarefa.'; }
    });
  }

  // ── Drag & Drop ───────────────────────────────────────────────

  private listaParaStatus: Record<string, string> = {
    'lista-pendentes':   'PENDENTE',
    'lista-em-execucao': 'EM_EXECUCAO',
    'lista-concluidas':  'CONCLUIDA'
  };

  drop(event: CdkDragDrop<Tarefa[]>): void {
    if (event.previousContainer === event.container) return;

    const tarefa: Tarefa = event.previousContainer.data[event.previousIndex];
    const novoStatus = this.listaParaStatus[event.container.id];

    // Move visualmente (feedback imediato)
    transferArrayItem(
      event.previousContainer.data,
      event.container.data,
      event.previousIndex,
      event.currentIndex
    );

    // Persiste no backend
    this.service.atualizarStatus(tarefa.id, novoStatus as StatusTarefa).subscribe({
      error: () => {
        this.erro = 'Erro ao mover tarefa. Recarregando...';
        this.carregarTarefas();
      }
    });
  }

  // ── Tema ──────────────────────────────────────────────────────

  alternarTema(): void {
    this.modoEscuro = !this.modoEscuro;
    localStorage.setItem(this.CHAVE_TEMA, this.modoEscuro ? 'escuro' : 'claro');
    this.aplicarTema();
  }

  private aplicarTema(): void {
    document.body.classList.toggle('dark', this.modoEscuro);
  }

  fecharErro(): void { this.erro = ''; }

  onKeydown(e: KeyboardEvent): void { if (e.key === 'Enter') this.criar(); }
}
