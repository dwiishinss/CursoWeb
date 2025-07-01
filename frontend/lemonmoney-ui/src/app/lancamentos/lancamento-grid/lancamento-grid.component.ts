import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { TooltipModule } from 'primeng/tooltip';

@Component({
  selector: 'app-lancamento-grid',
  templateUrl: './lancamento-grid.component.html',
  styleUrl: './lancamento-grid.component.css'
})
export class LancamentoGridComponent {

  @Input() lancamentos: {
  tipo: string;
  descricao: string;
  dataVencimento: Date;
  dataPagamento: Date | null;
  valor: number;
  pessoa: string;
}[] = []

}
