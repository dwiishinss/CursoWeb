import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Pessoa } from "../model/Pessoa";
import { firstValueFrom } from "rxjs";
import { Categoria } from "../model/Lancamento";

@Injectable()
export class CategoriasService { 

    categoriaUrl = 'http://localhost:8080/categorias'

    constructor(private http: HttpClient){ }

    consultarCategoria() {
        const headers = new HttpHeaders({
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBsZW1vbi5jb20iLCJpYXQiOjE3NTIwNzM1OTAsImV4cCI6MTc1MjA3NTM5MH0.-sZrmcNxATNor7ozJij_nIlthUYxjS-Pq4g9Srjjorc',
        'Content-Type': 'application/json'
        });
        return firstValueFrom(this.http.get<Categoria[]>(this.categoriaUrl, {headers})).then(
            response => { return response } 
        );
    }

}
