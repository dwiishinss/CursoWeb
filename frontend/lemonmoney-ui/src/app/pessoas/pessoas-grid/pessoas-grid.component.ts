import { Component, Input, ViewChild } from '@angular/core';
import { PessoaFiltro } from '../../services/pessoas.service';
import { TableLazyLoadEvent } from 'primeng/table';

@Component({
  selector: 'app-pessoas-grid',
  templateUrl: './pessoas-grid.component.html',
  styleUrl: './pessoas-grid.component.css'
})
export class PessoasGridComponent {

  @Input() pessoas: {}[] = []
  @Input() filtro: PessoaFiltro = new PessoaFiltro;
  @Input() totalRegistros = 0;
  @Input() pesquisar: (pagina?: number) => void = () => {};
  @Input() excluir: (pessoa: any, grid: any) => void = () => {};
  @Input() atualizarAtividade: (pessoa: any, grid: any) => void = () => {};
  
  @ViewChild('tabela') grid: any;

  aoMudarPagina(event: TableLazyLoadEvent){
    if(event != undefined && event.first != undefined && event.rows != undefined){
      const pagina = event.first/event.rows;
      this.pesquisar(pagina)
    }
  }

  aoExcluirPessoa(pessoa: any){
    this.excluir(pessoa, this.grid);
  }

}
