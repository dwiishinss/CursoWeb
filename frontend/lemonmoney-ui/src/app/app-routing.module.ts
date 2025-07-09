import { RouterModule, Routes } from "@angular/router";
import { LancamentosPesquisaComponent } from "./lancamentos/lancamentos-pesquisa/lancamentos-pesquisa.component";
import { LancamentoCadastroComponent } from "./lancamentos/lancamento-cadastro/lancamento-cadastro.component";
import { PessoasPesquisaComponent } from "./pessoas/pessoas-pesquisa/pessoas-pesquisa.component";
import { PessoaCadastroComponent } from "./pessoas/pessoa-cadastro/pessoa-cadastro.component";
import { PaginaNaoEncontrodaComponent } from "./core/pagina-nao-encontroda.component";
import { NgModule } from "@angular/core";

const routes: Routes = [
  {path: '', redirectTo: 'lancamentos', pathMatch: 'full'},
  {path: 'lancamentos', component: LancamentosPesquisaComponent},
  {path: 'lancamentos/novo', component: LancamentoCadastroComponent},
  {path: 'lancamentos/:id', component: LancamentoCadastroComponent},
  {path: 'pessoas', component: PessoasPesquisaComponent},
  {path: 'pessoas/novo', component: PessoaCadastroComponent},
  {path: 'pessoas/:id', component: LancamentoCadastroComponent},
  {path: 'pagina-nao-encontrada', component: PaginaNaoEncontrodaComponent},
  {path: '**', redirectTo: 'pagina-nao-encontrada', pathMatch: 'full'}
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes),
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule { }
