import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from 'rxjs';
import { Pessoa } from "../model/Pessoa";

export class PessoaFiltro {
  nome?: string;
  pagina = 0;
  itensPorPagina = 5;
}

@Injectable()
export class PessoasService { 

    pessoas: Pessoa[] = [];

    constructor(private http: HttpClient){ }

    async consultar(filter: PessoaFiltro) {
        let params = new HttpParams();
        const headers = new HttpHeaders({
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBsZW1vbi5jb20iLCJpYXQiOjE3NTE0ODk1MzAsImV4cCI6MTc1MTQ5MTMzMH0.5cOrJKmgbmot-j6hkGI9TKaTPYbRg4bA3ZR5m1Z1nUc',
        'Content-Type': 'application/json'
        });

        params = params.set('page', filter.pagina);
        params = params.set('size', filter.itensPorPagina);

        if(filter.nome){
          params = params.set('nome', filter.nome);
        }

        return await firstValueFrom(this.http.get<PessoaResponse>("http://localhost:8080/pessoas", {headers, params})).then(
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

    async consultarAtivo() {
        const headers = new HttpHeaders({
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBsZW1vbi5jb20iLCJpYXQiOjE3NTE0ODk1MzAsImV4cCI6MTc1MTQ5MTMzMH0.5cOrJKmgbmot-j6hkGI9TKaTPYbRg4bA3ZR5m1Z1nUc',
        'Content-Type': 'application/json'
        });

        return await firstValueFrom(this.http.get<Pessoa[]>("http://localhost:8080/pessoas/ativo", {headers})).then(
            response => { return response } 
        );
    }

    async adicionar(pessoa : Pessoa){
        const headers = new HttpHeaders({
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBsZW1vbi5jb20iLCJpYXQiOjE3NTE0ODk1MzAsImV4cCI6MTc1MTQ5MTMzMH0.5cOrJKmgbmot-j6hkGI9TKaTPYbRg4bA3ZR5m1Z1nUc',
        'Content-Type': 'application/json'
        });
        console.log(pessoa)
        await firstValueFrom(this.http.post("http://localhost:8080/lancamentos", pessoa ,{headers}));
    }

}

interface PessoaResponse {
  content: Pessoa[];
  totalPages: number;
  totalElements: number;
  // outras propriedades do seu objeto de resposta, se houver
}