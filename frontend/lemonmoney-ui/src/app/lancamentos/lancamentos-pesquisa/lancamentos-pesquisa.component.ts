import { Component, OnInit } from '@angular/core';
import { LancamentoFiltro, LancamentosService } from '../../services/lancamentos.service';
import { Lancamento } from '../../model/lancamento';

@Component({
  selector: 'app-lancamentos-pesquisa',
  templateUrl: './lancamentos-pesquisa.component.html',
  styleUrl: './lancamentos-pesquisa.component.css'
})
export class LancamentosPesquisaComponent{

  lancamentos: Lancamento[]= [];
  filtro = new LancamentoFiltro();
  totalRegistros = 0;
    
  constructor(private lancamentoService: LancamentosService){ }

  pesquisar(pagina = 0){
    this.filtro.pagina = pagina
    this.lancamentoService.consultar(this.filtro).then(
      dados => {
        this.lancamentos = dados.lancamentos;
        this.totalRegistros = dados.total;
      }
    )
  }

}
