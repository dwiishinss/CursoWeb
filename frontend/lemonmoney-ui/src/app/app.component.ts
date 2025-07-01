import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LancamentosPesquisaComponent } from './lancamentos/lancamentos-pesquisa/lancamentos-pesquisa.component';
import { NavbarComponent } from "./navbar/navbar.component";
import { PessoasPesquisaComponent } from "./pessoas-pesquisa/pessoas-pesquisa.component";
import { PessoaCadastroComponent } from './pessoa-cadastro/pessoa-cadastro.component';
import { LancamentoCadastroComponent } from './lancamentos/lancamento-cadastro/lancamento-cadastro.component';
import { LancamentosModule } from './lancamentos/lancamentos.module';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [LancamentosModule, NavbarComponent, PessoasPesquisaComponent, PessoaCadastroComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  
}
