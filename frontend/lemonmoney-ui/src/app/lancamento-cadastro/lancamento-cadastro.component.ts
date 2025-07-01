import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { CurrencyMaskModule } from 'ng2-currency-mask';
import { ButtonModule } from 'primeng/button';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { SelectButtonModule } from 'primeng/selectbutton';
import { TooltipModule } from 'primeng/tooltip';

@Component({
  selector: 'app-lancamento-cadastro',
  standalone: true,
  imports: [InputTextModule, InputTextareaModule, CalendarModule, SelectButtonModule, DropdownModule, ButtonModule, CurrencyMaskModule, CommonModule, TooltipModule],
  templateUrl: './lancamento-cadastro.component.html',
  styleUrl: './lancamento-cadastro.component.css'
})
export class LancamentoCadastroComponent {

  tipos = [
    {label:"Receita", value: "RECEITA"},
    {label:"Despesa", value: "DESPESA"}
  ];

  categorias = [
    {label:"Alimentação", value: 1},
    {label:"Transporte", value: 2}
  ];

  pessoas = [
    {label:"Igor", value: 1},
    {label:"Andre", value: 2}
  ];

}
