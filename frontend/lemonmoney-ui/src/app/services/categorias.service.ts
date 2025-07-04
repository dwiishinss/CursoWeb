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
        'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBsZW1vbi5jb20iLCJpYXQiOjE3NTE2NTYzMzAsImV4cCI6MTc1MTY1ODEzMH0.2F0-xWJcmHKYMak5f5oGe6B5KNzIUyqPVv7vKw5javQ',
        'Content-Type': 'application/json'
        });
        return firstValueFrom(this.http.get<Categoria[]>(this.categoriaUrl, {headers})).then(
            response => { return response } 
        );
    }

}
