import { Component, OnInit } from '@angular/core';
import { LancamentoFiltro, LancamentosService } from '../../services/lancamentos.service';
import { Lancamento } from '../../model/Lancamento';
import { ToastrService } from 'ngx-toastr';
import { ConfirmationService } from 'primeng/api';
import { ErrorHandlerService } from '../../core/error-handler.service';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-lancamentos-pesquisa',
  templateUrl: './lancamentos-pesquisa.component.html',
  styleUrl: './lancamentos-pesquisa.component.css'
})
export class LancamentosPesquisaComponent implements OnInit{

  lancamentos: Lancamento[]= [];
  filtro = new LancamentoFiltro();
  totalRegistros = 0;
    
  constructor(
    private lancamentoService: LancamentosService,
    private toasty: ToastrService,
    private confirmation: ConfirmationService,
    private errorHandle: ErrorHandlerService,
    private title: Title){ }

  ngOnInit(){
    this.title.setTitle('Pesquisa de lançamentos')
  }

  pesquisar(pagina = 0){
    this.filtro.pagina = pagina
    this.lancamentoService.consultar(this.filtro).then(
      dados => {
        this.lancamentos = dados.lancamentos;
        this.totalRegistros = dados.total;
      }
    ).catch(erro => this.errorHandle.handle(erro))
  }

  confirmarExclusao(lancamento: any, grid:any){
    this.confirmation.confirm({
      message: "Tem certeza que deseja excluir",
      accept: () => {
        this.excluir(lancamento, grid);
      }
    })
  }

  excluir(lancamento: any, grid: any){
    this.lancamentoService.excluir(lancamento.id).then(() => {
      grid.first = 0;
      this.pesquisar();

      this.toasty.success(
        'Lançamento excluído com sucesso!'
      )
    }).catch(erro => this.errorHandle.handle(erro))
  }

}
