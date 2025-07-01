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
import { InputMaskModule } from 'primeng/inputmask';
import { MessageComponent } from "../message/message.component";
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-pessoa-cadastro',
  standalone: true,
  imports: [InputTextModule, InputTextareaModule, CalendarModule, SelectButtonModule,
    DropdownModule, ButtonModule, CurrencyMaskModule, InputMaskModule, FormsModule,
     CommonModule, TooltipModule, MessageComponent],
  templateUrl: './pessoa-cadastro.component.html',
  styleUrl: './pessoa-cadastro.component.css'
})
export class PessoaCadastroComponent {

}
