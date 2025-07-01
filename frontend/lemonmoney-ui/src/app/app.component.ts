import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LancamentosPesquisaComponent } from './lancamentos-pesquisa/lancamentos-pesquisa.component';
import { NavbarComponent } from "./navbar/navbar.component";
import { PessoasPesquisaComponent } from "./pessoas-pesquisa/pessoas-pesquisa.component";
import { LancamentoCadastroComponent } from './lancamento-cadastro/lancamento-cadastro.component';
import { PessoaCadastroComponent } from './pessoa-cadastro/pessoa-cadastro.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [LancamentosPesquisaComponent, NavbarComponent, PessoasPesquisaComponent, PessoaCadastroComponent, LancamentoCadastroComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  
}
