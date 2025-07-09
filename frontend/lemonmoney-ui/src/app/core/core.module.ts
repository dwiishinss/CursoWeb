import { LOCALE_ID, NgModule } from '@angular/core';
import localePt from '@angular/common/locales/pt';
import { CommonModule, registerLocaleData } from '@angular/common';
import { NavbarComponent } from './navbar/navbar.component';
import { ErrorHandlerService } from './error-handler.service';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastrModule } from 'ngx-toastr';
import { LancamentosService } from '../services/lancamentos.service';
import { PessoasService } from '../services/pessoas.service';
import { ConfirmationService } from 'primeng/api';
import { CategoriasService } from '../services/categorias.service';
import { RouterModule } from '@angular/router';
import { PaginaNaoEncontrodaComponent } from './pagina-nao-encontroda.component';
import { Title } from '@angular/platform-browser';

registerLocaleData(localePt, 'pt-BR');

@NgModule({
  declarations: [NavbarComponent, PaginaNaoEncontrodaComponent],
  imports: [
    CommonModule,
    ConfirmDialogModule,
    RouterModule, 

    ToastrModule.forRoot({
      positionClass: 'toast-top-right',
      preventDuplicates: true,
      progressBar: true,
      timeOut: 3000
    }),
  ],
  exports: [
    NavbarComponent,
    ToastrModule,
    ConfirmDialogModule,
  ],
  providers: [
    ErrorHandlerService,
    LancamentosService, 
    PessoasService, 
    CategoriasService,
    ConfirmationService, 
    Title,
    {provide: LOCALE_ID, useValue: 'pt-br'}
  ]
})
export class CoreModule { }
