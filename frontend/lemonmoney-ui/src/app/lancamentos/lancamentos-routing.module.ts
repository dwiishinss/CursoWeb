import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { LancamentosPesquisaComponent } from "./lancamentos-pesquisa/lancamentos-pesquisa.component";
import { LancamentoCadastroComponent } from "./lancamento-cadastro/lancamento-cadastro.component";

const routes: Routes = [
  {path: 'lancamentos', component: LancamentosPesquisaComponent},
  {path: 'lancamentos/novo', component: LancamentoCadastroComponent},
  {path: 'lancamentos/:id', component: LancamentoCadastroComponent},
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [RouterModule],
})
export class LancamentosRoutingModule { }
