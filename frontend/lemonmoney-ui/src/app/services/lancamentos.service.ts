import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from 'rxjs';

import moment from 'moment';
import { Categoria, Lancamento } from "../model/Lancamento";

export class LancamentoFiltro {
  descricao?: string;
  dataVencimentoInicio?: Date;
  dataVencimentoFim?: Date;
  pagina = 0;
  itensPorPagina = 5;
}

@Injectable()
export class LancamentosService { 

    lancamentosUrl = 'http://localhost:8080/lancamentos'

    lancamentos: Lancamento[] = [];

    constructor(private http: HttpClient){ }

    consultar(filter: LancamentoFiltro) {
        let params = new HttpParams();
        const headers = new HttpHeaders({
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBsZW1vbi5jb20iLCJpYXQiOjE3NTE2NTYzMzAsImV4cCI6MTc1MTY1ODEzMH0.2F0-xWJcmHKYMak5f5oGe6B5KNzIUyqPVv7vKw5javQ',
        'Content-Type': 'application/json'
        });

        params = params.set('page', filter.pagina);
        params = params.set('size', filter.itensPorPagina);
        
        if(filter.descricao){
          params = params.set('descricao', filter.descricao);
        }
        if(filter.dataVencimentoInicio){
          params = params.set('dataVencimentoDe', moment(filter.dataVencimentoInicio).format('YYYY-MM-DD'));
        }
        if(filter.dataVencimentoFim){
          params = params.set('dataVencimentoAte', moment(filter.dataVencimentoFim).format('YYYY-MM-DD'));
        }

        return firstValueFrom(this.http.get<LancamentoResponse>(`${this.lancamentosUrl}?resumo`, { headers, params })).then(
            response => { 
              const lancamentos = response.content
              const resultado = {
                lancamentos,
                total: response.totalElements
              }
              return resultado
            } 
        );
    }

    adicionar(lancamento : Lancamento){
        const headers = new HttpHeaders({
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBsZW1vbi5jb20iLCJpYXQiOjE3NTE2NTYzMzAsImV4cCI6MTc1MTY1ODEzMH0.2F0-xWJcmHKYMak5f5oGe6B5KNzIUyqPVv7vKw5javQ',
        'Content-Type': 'application/json'
        });
        console.log(lancamento)
        firstValueFrom(this.http.post(`${this.lancamentosUrl}`, lancamento ,{headers}));
    }

    excluir(codigo: number) {
        const headers = new HttpHeaders({
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBsZW1vbi5jb20iLCJpYXQiOjE3NTE2NTYzMzAsImV4cCI6MTc1MTY1ODEzMH0.2F0-xWJcmHKYMak5f5oGe6B5KNzIUyqPVv7vKw5javQ',
        'Content-Type': 'application/json'
        });
        
        return firstValueFrom(this.http.delete(`${this.lancamentosUrl}/${codigo}`, { headers })).then(() => null)
    }

}

interface LancamentoResponse {
  content: Lancamento[];
  totalPages: number;
  totalElements: number;
  // outras propriedades do seu objeto de resposta, se houver
}