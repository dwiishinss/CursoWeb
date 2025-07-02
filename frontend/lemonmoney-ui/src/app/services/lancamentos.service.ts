import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from 'rxjs';
import { Categoria, Lancamento } from "../model/lancamento";

import moment from 'moment';

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

    async consultar(filter: LancamentoFiltro) {
        let params = new HttpParams();
        const headers = new HttpHeaders({
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBsZW1vbi5jb20iLCJpYXQiOjE3NTE0ODk1MzAsImV4cCI6MTc1MTQ5MTMzMH0.5cOrJKmgbmot-j6hkGI9TKaTPYbRg4bA3ZR5m1Z1nUc',
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

        return await firstValueFrom(this.http.get<LancamentoResponse>(`${this.lancamentosUrl}?resumo`, { headers, params })).then(
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

    async consultarCategoria() {
        const headers = new HttpHeaders({
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBsZW1vbi5jb20iLCJpYXQiOjE3NTE0ODk1MzAsImV4cCI6MTc1MTQ5MTMzMH0.5cOrJKmgbmot-j6hkGI9TKaTPYbRg4bA3ZR5m1Z1nUc',
        'Content-Type': 'application/json'
        });
        return await firstValueFrom(this.http.get<Categoria[]>("http://localhost:8080/categorias", {headers})).then(
            response => { return response } 
        );
    }

    async adicionar(lancamento : Lancamento){
        const headers = new HttpHeaders({
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBsZW1vbi5jb20iLCJpYXQiOjE3NTE0ODk1MzAsImV4cCI6MTc1MTQ5MTMzMH0.5cOrJKmgbmot-j6hkGI9TKaTPYbRg4bA3ZR5m1Z1nUc',
        'Content-Type': 'application/json'
        });
        console.log(lancamento)
        await firstValueFrom(this.http.post("http://localhost:8080/lancamentos", lancamento ,{headers}));
    }

}

interface LancamentoResponse {
  content: Lancamento[];
  totalPages: number;
  totalElements: number;
  // outras propriedades do seu objeto de resposta, se houver
}