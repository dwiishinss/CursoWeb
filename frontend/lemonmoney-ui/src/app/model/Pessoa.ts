export class Pessoa {
  id?: number;
  nome?: string;
  ativo?: boolean;
  endereco?: Endereco;
}

export class Endereco {
  logradouro?: string | null;
  numero?: string;
  complemento?: string | null;
  bairro?: string;
  cep?: string;
  cidade?: string | null;
  estado?: string | null;
}
