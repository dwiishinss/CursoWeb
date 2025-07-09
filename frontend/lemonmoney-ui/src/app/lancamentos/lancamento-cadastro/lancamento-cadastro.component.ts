import { Component, OnInit } from '@angular/core';
import { LancamentosService } from '../../services/lancamentos.service';
import { Categoria, Lancamento } from '../../model/Lancamento';
import { PessoasService } from '../../services/pessoas.service';
import { CategoriasService } from '../../services/categorias.service';
import { ErrorHandlerService } from '../../core/error-handler.service';
import { ToastrService } from 'ngx-toastr';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { error } from 'node:console';
import { Pessoa } from '../../model/Pessoa';
import { Title } from '@angular/platform-browser';


@Component({
  selector: 'app-lancamento-cadastro',
  templateUrl: './lancamento-cadastro.component.html',
  styleUrl: './lancamento-cadastro.component.css'
})
export class LancamentoCadastroComponent implements OnInit{

  lancamento = new Lancamento
  dataVencimento: Date|null = null
  dataPagamento: Date|null = null

  constructor(private lancamentoService: LancamentosService, 
    private pessoasService: PessoasService,
    private categoriasSevice: CategoriasService,
    private toasty: ToastrService,
    private errorHandle: ErrorHandlerService,
    private route: ActivatedRoute,
    private router: Router,
    private title: Title){ }

  adicionar(lancamentoForm: NgForm){

    this.lancamento.dataVencimento = toISODate(this.dataVencimento)
    this.lancamento.dataPagamento = toISODate(this.dataPagamento)
    
    if(this.route.snapshot.params['id']){
      this.lancamento.id = this.route.snapshot.params['id']
      this.lancamentoService.editar(this.lancamento).then(
        () => {
          this.toasty.success(
            'Lançamento alterado com sucesso!'
          )
          this.atualizarTituloEdicao()
        }
      )
      .catch(error => this.errorHandle.handle(error))
    }else{
      this.lancamentoService.adicionar(this.lancamento).then(
        lancamentoNovo => {
          this.toasty.success(
            'Lançamento salvo com sucesso!'
          )
          this.lancamento = lancamentoNovo

          this.router.navigate(['/lancamentos', this.lancamento.id])
        }
      )
      .catch(error => this.errorHandle.handle(error))
    }
  }

  ngOnInit(){
    this.title.setTitle('Novo lançamento')

    const idLancamento = this.route.snapshot.params['id']

    this.pessoasService.consultarAtivo().then(
      dados => {
        this.pessoas = dados.map((p: any) => ({
          label: p.nome,
          value: p
        }));
        }).catch(erro => this.errorHandle.handle(erro))
    this.categoriasSevice.consultarCategoria().then(
      dados => {
        this.categorias = dados.map((p: any) => ({
          label: p.nome,
          value: p
        }));
      }).catch(erro => this.errorHandle.handle(erro))
    
    if(idLancamento) {
      this.carregarLancamento(idLancamento)
    }
  }

  get editando(){
    return Boolean(this.lancamento.id)
  }

  atualizarTituloEdicao(){
    this.title.setTitle(`Edição de lancamento: ${this.lancamento.descricao}`)
  }

  carregarLancamento(id: number){
    this.lancamentoService.consultarId(id)
    .then(lancamento => {
      this.lancamento = lancamento
      if(this.lancamento.dataVencimento){
        this.dataVencimento = new Date(this.lancamento.dataVencimento)
      }
      if(this.lancamento.dataPagamento){
        this.dataPagamento = new Date(this.lancamento.dataPagamento)
      }
      this.atualizarTituloEdicao()
    })
    .catch(error => this.errorHandle.handle(error))

  }

  novo(form: NgForm){
    form.reset()
    setTimeout(() =>{
      this.lancamento = new Lancamento()
    }, 1)
    this.router.navigate(['/lancamentos/novo'])
  }

  pessoas = [
    {label:"Igor", value: {id: 1}},
    {label:"Andre", value: {id: 2}}
  ];

  tipos = [
    {label:"Receita", value: "RECEITA"},
    {label:"Despesa", value: "DESPESA"}
  ];

  categorias = [
    {label:"Alimentação", value: {id: 2}},
    {label:"Transporte", value: {id: 3}}
  ];

  

}

function toISODate(date: any): string | null {
  if (!date) return null;
  const d = new Date(date);
  return d.toISOString().split('T')[0];
}