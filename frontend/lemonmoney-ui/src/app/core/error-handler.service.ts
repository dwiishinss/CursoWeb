import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { error } from 'console';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  constructor(private toasty: ToastrService) { }

  handle(errorResponse: any) {
    let msg: string;

    if(typeof errorResponse === 'string'){
      msg = errorResponse
    }else if(errorResponse instanceof  HttpErrorResponse && errorResponse.status >= 400 && errorResponse.status < 500){
      msg = `${errorResponse.status} - ${errorResponse.error[0].mensagemUsuario}`
      console.log('Ocorreu um erro', errorResponse.error[0].mensagemDesenvolvedor)
    }else{
      console.log(typeof errorResponse)
      msg = 'Erro ao processar serviço remoto. Tente novamente.';
      console.log('Ocorreu um erro', errorResponse)
    }

    this.toasty.error(msg);
  }

}
