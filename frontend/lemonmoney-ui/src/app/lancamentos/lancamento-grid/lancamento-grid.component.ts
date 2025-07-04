import { Component, Input, ViewChild } from '@angular/core';
import { Lancamento } from '../../model/Lancamento';
import { LancamentoFiltro } from '../../services/lancamentos.service';
import { TableLazyLoadEvent } from 'primeng/table';
import { LancamentosPesquisaComponent } from '../lancamentos-pesquisa/lancamentos-pesquisa.component';

@Component({
  selector: 'app-lancamento-grid',
  templateUrl: './lancamento-grid.component.html',
  styleUrl: './lancamento-grid.component.css'
})
export class LancamentoGridComponent {

  @Input() lancamentos: Lancamento[] = []
  @Input() filtro: LancamentoFiltro = new LancamentoFiltro;
  @Input() totalRegistros = 0;
  @Input() pesquisar: (pagina?: number) => void = () => {};
  @Input() excluir: (lancamento: any, grid: any) => void = () => {};

  @ViewChild('tabela') grid: any;
  
  aoMudarPagina(event: TableLazyLoadEvent){
    if(event != undefined && event.first != undefined && event.rows != undefined){
      const pagina = event.first/event.rows;
      this.pesquisar(pagina)
    }
  }

  aoExcluirLancamento(lancamento: any){
    this.excluir(lancamento, this.grid);
  }

}
