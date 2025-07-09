import { Component, OnInit } from '@angular/core';
import { Pessoa } from '../../model/Pessoa';
import { PessoaFiltro, PessoasService } from '../../services/pessoas.service';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';
import { ToastrService } from 'ngx-toastr';
import { ErrorHandlerService } from '../../core/error-handler.service';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-pessoas-pesquisa',
  templateUrl: './pessoas-pesquisa.component.html',
  styleUrl: './pessoas-pesquisa.component.css'
})
export class PessoasPesquisaComponent implements OnInit{
  
  pessoas: Pessoa[]= [];
  filtro = new PessoaFiltro();
  totalRegistros = 0;
    
  constructor(
    private pessoaService: PessoasService,
    private confirmation: ConfirmationService,
    private toasty: ToastrService,
    private errorHandle: ErrorHandlerService,
    private title: Title
  ){ }


  ngOnInit(): void {
    this.title.setTitle('Pesquisa pessoas')
  }

  pesquisar(pagina = 0){
    this.filtro.pagina = pagina
    this.pessoaService.consultar(this.filtro).then(
      dados => {
        this.pessoas = dados.pessoas;
        this.totalRegistros = dados.total;
      }
    )
  }

  atualizarAtividade(pessoa: any, grid: any){
    this.pessoaService.modificarAtividade(pessoa).then(() => {
      this.pesquisar(grid.first)
    })
  }

  confirmarExclusao(pessoa: any, grid:any){
    this.confirmation.confirm({
      message: "Tem certeza que deseja excluir",
      accept: () => {
        this.excluir(pessoa, grid);
      }
    })
  }

  excluir(pessoa: any, grid: any){
    this.pessoaService.excluir(pessoa.id).then(() => {
      grid.first = 0;
      this.pesquisar();

      this.toasty.success(
        'Lançamento excluído com sucesso!'
      )
    }).catch(erro => this.errorHandle.handle(erro))
  }

}
