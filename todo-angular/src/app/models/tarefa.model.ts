export type StatusTarefa = 'pendente' | 'em_execucao' | 'concluida';

export interface Tarefa {
  id: number;
  titulo: string;
  status: StatusTarefa;
  dataCriacao: string;       // vem como string ISO do backend
  dataAtualizacao: string;
}
