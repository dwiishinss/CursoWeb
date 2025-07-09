import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from 'rxjs';
import { Pessoa } from "../model/Pessoa";
import { response } from "express";

export class PessoaFiltro {
  nome?: string;
  pagina = 0;
  itensPorPagina = 5;
}

@Injectable()
export class PessoasService { 

    pessoas: Pessoa[] = [];
    pessoasUrl = 'http://localhost:8080/pessoas'

    constructor(private http: HttpClient){ }

    async consultar(filter: PessoaFiltro) {
        let params = new HttpParams();
        const headers = new HttpHeaders({
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBsZW1vbi5jb20iLCJpYXQiOjE3NTIwNzM1OTAsImV4cCI6MTc1MjA3NTM5MH0.-sZrmcNxATNor7ozJij_nIlthUYxjS-Pq4g9Srjjorc',
        'Content-Type': 'application/json'
        });

        params = params.set('page', filter.pagina);
        params = params.set('size', filter.itensPorPagina);

        if(filter.nome){
          params = params.set('nome', filter.nome);
        }

        return await firstValueFrom(this.http.get<PessoaResponse>(this.pessoasUrl, {headers, params})).then(
            response => { 
              const pessoas = response.content
              console.log(pessoas)
              const resultado = {
                pessoas,
                total: response.totalElements
              }
              return resultado
            }  
        );
    }

    consultarAtivo() {
        const headers = new HttpHeaders({
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBsZW1vbi5jb20iLCJpYXQiOjE3NTIwNzM1OTAsImV4cCI6MTc1MjA3NTM5MH0.-sZrmcNxATNor7ozJij_nIlthUYxjS-Pq4g9Srjjorc',
        'Content-Type': 'application/json'
        });

        return firstValueFrom(this.http.get<Pessoa[]>(`${this.pessoasUrl}/ativo`, {headers})).then(
            response => { return response } 
        );
    }

    consultarId(id: number) {
        const headers = new HttpHeaders({
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBsZW1vbi5jb20iLCJpYXQiOjE3NTIwNzM1OTAsImV4cCI6MTc1MjA3NTM5MH0.-sZrmcNxATNor7ozJij_nIlthUYxjS-Pq4g9Srjjorc',
        'Content-Type': 'application/json'
        });

        return firstValueFrom(this.http.get(`${this.pessoasUrl}/${id}`, { headers })).then(
            response => { 
              return response
            } 
        );
    }

    editar(pessoa : Pessoa){
        const headers = new HttpHeaders({
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBsZW1vbi5jb20iLCJpYXQiOjE3NTIwNzM1OTAsImV4cCI6MTc1MjA3NTM5MH0.-sZrmcNxATNor7ozJij_nIlthUYxjS-Pq4g9Srjjorc',
        'Content-Type': 'application/json'
        });
        return firstValueFrom(this.http.put(`${this.pessoasUrl}/${pessoa.id}`, pessoa ,{headers}));
    }

    modificarAtividade(pessoa: Pessoa) {
      const headers = new HttpHeaders({
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBsZW1vbi5jb20iLCJpYXQiOjE3NTIwNzM1OTAsImV4cCI6MTc1MjA3NTM5MH0.-sZrmcNxATNor7ozJij_nIlthUYxjS-Pq4g9Srjjorc',
        'Content-Type': 'application/json'
      });

      return firstValueFrom(this.http.put(`${this.pessoasUrl}/${pessoa.id}/ativo`, !pessoa.ativo, {headers})).then(
        response => { return response }
      )
    }

    adicionar(pessoa : Pessoa){
        const headers = new HttpHeaders({
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBsZW1vbi5jb20iLCJpYXQiOjE3NTIwNzM1OTAsImV4cCI6MTc1MjA3NTM5MH0.-sZrmcNxATNor7ozJij_nIlthUYxjS-Pq4g9Srjjorc',
        'Content-Type': 'application/json'
        });
        console.log(pessoa)
        return firstValueFrom(this.http.post(this.pessoasUrl, pessoa ,{headers}));
    }

    excluir(codigo: number) {
        const headers = new HttpHeaders({
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBsZW1vbi5jb20iLCJpYXQiOjE3NTIwNzM1OTAsImV4cCI6MTc1MjA3NTM5MH0.-sZrmcNxATNor7ozJij_nIlthUYxjS-Pq4g9Srjjorc',
        'Content-Type': 'application/json'
        });
        
        return firstValueFrom(this.http.delete(`${this.pessoasUrl}/${codigo}`, { headers })).then(() => null)
    }

}

interface PessoaResponse {
  content: Pessoa[];
  totalPages: number;
  totalElements: number;
  // outras propriedades do seu objeto de resposta, se houver
}