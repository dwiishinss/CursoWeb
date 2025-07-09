import { Pessoa } from "./Pessoa";

export class Lancamento {

    id?: number;                 
    descricao?: string;         
    dataVencimento?: string | null;     
    dataPagamento?: string | null; 
    valor?: number;              
    tipo?: 'RECEITA' | 'DESPESA' = 'RECEITA';
    observacao?: string | null;
    categoria?: Categoria;         
    pessoa?: Pessoa; 

}

export class Categoria {

    id?: number;
    descricao?: string;

}