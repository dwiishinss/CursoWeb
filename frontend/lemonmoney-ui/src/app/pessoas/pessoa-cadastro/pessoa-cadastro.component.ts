import { Component, OnInit } from '@angular/core';
import { Endereco, Pessoa } from '../../model/Pessoa';
import { NgForm } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { PessoasService } from '../../services/pessoas.service';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ErrorHandlerService } from '../../core/error-handler.service';

@Component({
  selector: 'app-pessoa-cadastro',
  templateUrl: './pessoa-cadastro.component.html',
  styleUrl: './pessoa-cadastro.component.css'
})
export class PessoaCadastroComponent implements OnInit{

  pessoa = new Pessoa()

  constructor (private pessoasService: PessoasService,
      private toasty: ToastrService,
      private errorHandle: ErrorHandlerService, 
      private route: ActivatedRoute,
      private router: Router,
      private title: Title
  ){
    this.pessoa.endereco = new Endereco()
    this.pessoa.ativo = true
  }
  ngOnInit(): void {
    this.title.setTitle("Nova pessoa")

    if(this.route.snapshot.params['id']){
      this.carregarPessoa(this.route.snapshot.params['id'])
    }
  }


  adicionar(pessoaForm: NgForm){
    if(this.route.snapshot.params['id']){
      this.pessoa.id = this.route.snapshot.params['id']
      this.pessoasService.editar(this.pessoa).then(
        () => {
          this.toasty.success(
            'Pessoa alterado com sucesso!'
          )
          this.atualizarTituloEdicao()
        }
      )
      .catch(error => this.errorHandle.handle(error));
    }else{
      this.pessoasService.adicionar(this.pessoa).then(
        pessoaNova => {
          this.pessoa = pessoaNova
          this.toasty.success(
            'Pessoa salvo com sucesso!'
          )
          this.router.navigate(['/pessoas', this.pessoa.id])
        }
      ).catch(error => this.errorHandle.handle(error));
    }
  }

  carregarPessoa(id: number){
    this.pessoasService.consultarId(id)
    .then(pessoa => {
      this.pessoa = pessoa
      this.atualizarTituloEdicao()
    })
    .catch(error => this.errorHandle.handle(error))

  }

  novo(form: NgForm){
    form.reset()
    this.pessoa = new Pessoa()
    this.router.navigate(['/pessoas/novo'])
  }

  get editando(){
    return Boolean(this.pessoa.id)
  }

  atualizarTituloEdicao(){
    this.title.setTitle(`Edição da pessoa: ${this.pessoa.nome}`)
  }

}
