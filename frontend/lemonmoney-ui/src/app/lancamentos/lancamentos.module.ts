import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LancamentoCadastroComponent } from './lancamento-cadastro/lancamento-cadastro.component';
import { LancamentosPesquisaComponent } from './lancamentos-pesquisa/lancamentos-pesquisa.component';
import { LancamentoGridComponent } from './lancamento-grid/lancamento-grid.component';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';
import { TableModule } from 'primeng/table';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { CalendarModule } from 'primeng/calendar';
import { SelectButtonModule } from 'primeng/selectbutton';
import { DropdownModule } from 'primeng/dropdown';
import { CurrencyMaskModule } from 'ng2-currency-mask';
import { FormsModule } from '@angular/forms';
import { SharedModule } from "../shared/shared.module";
import { LancamentosService } from '../services/lancamentos.service';
import { HttpClientModule } from '@angular/common/http';
import { PessoasService } from '../services/pessoas.service';



@NgModule({
  declarations: [LancamentoCadastroComponent, LancamentosPesquisaComponent, LancamentoGridComponent],
  imports: [
    InputTextModule,
    ButtonModule,
    CommonModule,
    TableModule,
    TooltipModule,
    InputTextareaModule,
    CalendarModule,
    SelectButtonModule,
    DropdownModule,
    CurrencyMaskModule,
    FormsModule,
    SharedModule,
],
  exports: [LancamentoCadastroComponent, LancamentosPesquisaComponent, HttpClientModule],
  providers: []
})
export class LancamentosModule { }
