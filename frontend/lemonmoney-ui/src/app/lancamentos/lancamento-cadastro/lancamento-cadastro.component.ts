import { Component, OnInit } from '@angular/core';
import { LancamentosService } from '../../services/lancamentos.service';
import { Categoria, Lancamento } from '../../model/lancamento';
import { Pessoa } from '../../model/Pessoa';
import { PessoasService } from '../../services/pessoas.service';


@Component({
  selector: 'app-lancamento-cadastro',
  templateUrl: './lancamento-cadastro.component.html',
  styleUrl: './lancamento-cadastro.component.css'
})
export class LancamentoCadastroComponent implements OnInit{

  tipoSelecionado = "RECEITA";

  constructor(private lancamentoService: LancamentosService, private pessoasService: PessoasService){ }

  adicionar(lancamento: Lancamento, vencimento: Date, pagamento: Date){

    lancamento.dataVencimento = toISODate(vencimento)
    lancamento.dataPagamento = toISODate(pagamento)
    
    const lancamentoInput : Lancamento = {
      descricao: lancamento.descricao,
      dataVencimento: lancamento.dataVencimento,
      dataPagamento: lancamento.dataPagamento,
      valor: lancamento.valor,
      tipo: lancamento.tipo,
      categoria: lancamento.categoria,
      pessoa: lancamento.pessoa,
      observacao: lancamento.observacao
    }
    this.lancamentoService.adicionar(lancamentoInput);
  }

  ngOnInit(){
    this.pessoasService.consultarAtivo().then(
      dados => {
        this.pessoas = dados.map((p: any) => ({
          label: p.nome,
          value: { id: p.id }
        }));
        })
    this.lancamentoService.consultarCategoria().then(
      dados => {
        this.categorias = dados.map((p: any) => ({
          label: p.nome,
          value: { id: p.id }
        }));
      })
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